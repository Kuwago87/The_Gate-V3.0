# apply-my-settings.ps1
#
# Run this from the repo root (the folder with common/, paper/, and spigot/ in it), any time
# after pulling fresh files, to reapply your bStats ID and version number.
#
# Usage:  right-click -> "Run with PowerShell", or from an open PowerShell window:  .\apply-my-settings.ps1

$ErrorActionPreference = "Stop"

# ---- EDIT THESE VALUES WHENEVER YOU WANT TO CHANGE WHAT GETS APPLIED ----
$BStatsId = "32964"
$BaseVersion = "3.0.6mc26.2"
$VariantSuffix = @{
    "paper"  = "-ppr"
    "spigot" = "-spig"
}
# ---------------------------------------------------------------------------

$root = $PSScriptRoot

Write-Host ""
Write-Host "Apply settings to:" -ForegroundColor Cyan
Write-Host "  [1] Paper only"
Write-Host "  [2] Spigot only"
Write-Host "  [3] Both (default)"
$choice = Read-Host "Enter a number and press Enter"

switch ($choice) {
    "1" { $variants = @("paper") }
    "2" { $variants = @("spigot") }
    default { $variants = @("paper", "spigot") }
}

Write-Host ""
Write-Host "Applying to: $($variants -join ', ')" -ForegroundColor Cyan
Write-Host ""

$totalChanges = 0
$totalChecks = 0

foreach ($variant in $variants) {
    $variantDir = Join-Path $root $variant
    if (-not (Test-Path $variantDir)) {
        Write-Host "[MISSING] Could not find '$variant' folder at $variantDir - skipping." -ForegroundColor Red
        continue
    }

    Write-Host "--- $variant ---" -ForegroundColor Yellow

    $PluginVersion = "$BaseVersion$($VariantSuffix[$variant])"
    Write-Host "  (version for this variant: $PluginVersion)"

    $mainFile = Join-Path $variantDir "src\main\java\thegate\main\TheGateMain.java"
    $pluginYml = Join-Path $variantDir "src\main\resources\plugin.yml"
    $pomFile = Join-Path $variantDir "pom.xml"

    # --- bStats ID ---
    $totalChecks++
    if (Test-Path $mainFile) {
        $content = Get-Content $mainFile -Raw
        $newContent = $content -replace 'int PluginIDBStats = \d+;', "int PluginIDBStats = $BStatsId;"
        if ($newContent -ne $content) {
            Set-Content $mainFile $newContent -NoNewline
            Write-Host "[OK] bStats ID set to $BStatsId in $variant\...\TheGateMain.java" -ForegroundColor Green
            $totalChanges++
        } else {
            Write-Host "[SKIP] bStats ID pattern not found or already set in $variant\...\TheGateMain.java" -ForegroundColor Yellow
        }
    } else {
        Write-Host "[MISSING] Could not find $mainFile" -ForegroundColor Red
    }

    # --- Plugin version (plugin.yml) ---
    $totalChecks++
    if (Test-Path $pluginYml) {
        $content = Get-Content $pluginYml -Raw
        $newContent = $content -replace '(?m)^version:\s*.*$', "version: $PluginVersion"
        if ($newContent -ne $content) {
            Set-Content $pluginYml $newContent -NoNewline
            Write-Host "[OK] Version set to $PluginVersion in $variant\...\plugin.yml" -ForegroundColor Green
            $totalChanges++
        } else {
            Write-Host "[SKIP] version: line not found in $variant\...\plugin.yml" -ForegroundColor Yellow
        }
    } else {
        Write-Host "[MISSING] Could not find $pluginYml" -ForegroundColor Red
    }

    # --- Project version (pom.xml) - only the project's OWN <version> tag ---
    $totalChecks++
    if (Test-Path $pomFile) {
        $content = Get-Content $pomFile -Raw
        $pattern = '(?s)(<artifactId>The_Gate</artifactId>\s*<version>)[^<]*(</version>)'
        $newContent = $content -replace $pattern, "`${1}$PluginVersion`${2}"
        if ($newContent -ne $content) {
            Set-Content $pomFile $newContent -NoNewline
            Write-Host "[OK] Version set to $PluginVersion in $variant\pom.xml" -ForegroundColor Green
            $totalChanges++
        } else {
            Write-Host "[SKIP] Project <version> tag not found in $variant\pom.xml" -ForegroundColor Yellow
        }
    } else {
        Write-Host "[MISSING] Could not find $pomFile" -ForegroundColor Red
    }

    Write-Host ""
}

Write-Host "Done. $totalChanges of $totalChecks checks resulted in a change." -ForegroundColor Cyan
if ($totalChanges -lt $totalChecks) {
    Write-Host "Some items were skipped or missing - check the messages above." -ForegroundColor Yellow
}
