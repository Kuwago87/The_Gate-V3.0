# build-variant.ps1
#
# Assembles a buildable Maven project from common/ + one or both platform variants,
# then runs `mvn clean package` for each.
#
# Usage:  right-click -> "Run with PowerShell", or from an open PowerShell window:  .\build-variant.ps1
# Run from the repo root, where common/, paper/, and spigot/ live side by side.

$ErrorActionPreference = "Stop"
$root = $PSScriptRoot

Write-Host ""
Write-Host "Build which variant?" -ForegroundColor Cyan
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
Write-Host "Building: $($variants -join ', ')" -ForegroundColor Cyan

$results = @{}

foreach ($variant in $variants) {
    $commonDir = Join-Path $root "common"
    $variantDir = Join-Path $root $variant
    $buildDir = Join-Path $root "dist-$variant"

    Write-Host ""
    Write-Host "=== $variant ===" -ForegroundColor Yellow

    if (-not (Test-Path $commonDir)) {
        Write-Host "[ERROR] Could not find 'common' folder at $commonDir - skipping $variant." -ForegroundColor Red
        $results[$variant] = "MISSING common/"
        continue
    }
    if (-not (Test-Path $variantDir)) {
        Write-Host "[ERROR] Could not find '$variant' folder at $variantDir - skipping $variant." -ForegroundColor Red
        $results[$variant] = "MISSING $variant/"
        continue
    }

    Write-Host "Assembling into $buildDir ..."

    # Start clean every time, so nothing stale lingers between builds
    if (Test-Path $buildDir) {
        Remove-Item $buildDir -Recurse -Force
    }
    New-Item -ItemType Directory -Path $buildDir | Out-Null

    # 1. Copy everything shared
    Copy-Item "$commonDir\*" $buildDir -Recurse -Force

    # 2. Copy the variant-specific files on top (overwrites pom.xml, plugin.yml, TheGateMain.java;
    #    adds SpigotEntityIdProvider.java for the spigot variant)
    Copy-Item "$variantDir\*" $buildDir -Recurse -Force

    Write-Host "[OK] Assembled. Running mvn clean package..." -ForegroundColor Green

    Push-Location $buildDir
    try {
        mvn clean package
        $mavenExitCode = $LASTEXITCODE
    } finally {
        Pop-Location
    }

    if ($mavenExitCode -eq 0) {
        Write-Host "[OK] $variant build succeeded. Jar is in dist-$variant\target\" -ForegroundColor Green
        $results[$variant] = "SUCCESS"
    } else {
        Write-Host "[FAILED] $variant build failed - see Maven output above." -ForegroundColor Red
        $results[$variant] = "FAILED"
    }
}

Write-Host ""
Write-Host "=== Summary ===" -ForegroundColor Cyan
foreach ($variant in $variants) {
    $status = $results[$variant]
    $color = if ($status -eq "SUCCESS") { "Green" } else { "Red" }
    Write-Host "  $variant : $status" -ForegroundColor $color
}