# build-variant.ps1
#
# Assembles a buildable Maven project from common/ + one platform variant, then runs `mvn clean package`.
#
# Usage examples (run from the repo root, where common/, paper/, and spigot/ live side by side):
#   .\build-variant.ps1 paper
#   .\build-variant.ps1 spigot

param(
    [Parameter(Mandatory=$true)]
    [ValidateSet("paper", "spigot")]
    [string]$Variant
)

$ErrorActionPreference = "Stop"
$root = $PSScriptRoot
$commonDir = Join-Path $root "common"
$variantDir = Join-Path $root $Variant
$buildDir = Join-Path $root "dist-$Variant"

if (-not (Test-Path $commonDir)) {
    Write-Host "[ERROR] Could not find 'common' folder at $commonDir" -ForegroundColor Red
    exit 1
}
if (-not (Test-Path $variantDir)) {
    Write-Host "[ERROR] Could not find '$Variant' folder at $variantDir" -ForegroundColor Red
    exit 1
}

Write-Host "Assembling $Variant build into $buildDir ..." -ForegroundColor Cyan

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
    mvn clean package -e
} finally {
    Pop-Location
}

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "[OK] Build succeeded. Jar is in dist-$Variant\target\" -ForegroundColor Green
} else {
    Write-Host ""
    Write-Host "[FAILED] Build failed - see Maven output above." -ForegroundColor Red
}
