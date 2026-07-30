# apply-my-settings.ps1
#
# Run this from the project's root folder (the one with pom.xml in it), every time
# you extract a fresh zip from Claude. It reapplies your own bStats ID and version
# number, so you don't have to manually redo it each time.
#
# Usage:  right-click -> "Run with PowerShell", or from an open PowerShell window:  .\apply-my-settings.ps1

$ErrorActionPreference = "Stop"

# ---- EDIT THESE TWO VALUES WHENEVER YOU WANT TO CHANGE WHAT GETS APPLIED ----
$BStatsId = "32964"
$PluginVersion = "3.0.6mc26.2"
# ------------------------------------------------------------------------------

$root = $PSScriptRoot
$mainFile = Join-Path $root "src\main\java\thegate\main\TheGateMain.java"
$pluginYml = Join-Path $root "src\main\resources\plugin.yml"
$pomFile = Join-Path $root "pom.xml"

$changesMade = 0

# --- 1. bStats ID (TheGateMain.java) ---
if (Test-Path $mainFile) {
    $content = Get-Content $mainFile -Raw
    $newContent = $content -replace 'int PluginIDBStats = \d+;', "int PluginIDBStats = $BStatsId;"
    if ($newContent -ne $content) {
        Set-Content $mainFile $newContent -NoNewline
        Write-Host "[OK] bStats ID set to $BStatsId in TheGateMain.java" -ForegroundColor Green
        $changesMade++
    } else {
        Write-Host "[SKIP] bStats ID pattern not found or already set in TheGateMain.java" -ForegroundColor Yellow
    }
} else {
    Write-Host "[MISSING] Could not find $mainFile" -ForegroundColor Red
}

# --- 2. Plugin version (plugin.yml) ---
if (Test-Path $pluginYml) {
    $content = Get-Content $pluginYml -Raw
    $newContent = $content -replace '(?m)^version:\s*.*$', "version: $PluginVersion"
    if ($newContent -ne $content) {
        Set-Content $pluginYml $newContent -NoNewline
        Write-Host "[OK] Version set to $PluginVersion in plugin.yml" -ForegroundColor Green
        $changesMade++
    } else {
        Write-Host "[SKIP] version: line not found in plugin.yml" -ForegroundColor Yellow
    }
} else {
    Write-Host "[MISSING] Could not find $pluginYml" -ForegroundColor Red
}

# --- 3. Project version (pom.xml) - only the project's OWN <version> tag, ---
#         not any dependency's <version> tag elsewhere in the file.
if (Test-Path $pomFile) {
    $content = Get-Content $pomFile -Raw
    $pattern = '(?s)(<artifactId>The_Gate</artifactId>\s*<version>)[^<]*(</version>)'
    $newContent = $content -replace $pattern, "`${1}$PluginVersion`${2}"
    if ($newContent -ne $content) {
        Set-Content $pomFile $newContent -NoNewline
        Write-Host "[OK] Version set to $PluginVersion in pom.xml" -ForegroundColor Green
        $changesMade++
    } else {
        Write-Host "[SKIP] Project <version> tag not found in pom.xml (check it wasn't already changed)" -ForegroundColor Yellow
    }
} else {
    Write-Host "[MISSING] Could not find $pomFile" -ForegroundColor Red
}

Write-Host ""
Write-Host "Done. $changesMade of 3 files updated." -ForegroundColor Cyan
if ($changesMade -lt 3) {
    Write-Host "Some files were skipped - check the messages above. This can happen if a value was already changed, or if file layout changed in a newer zip." -ForegroundColor Yellow
}
