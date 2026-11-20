# Prints the signing certificate and version of an APK using the newest installed build-tools.
param([Parameter(Mandatory = $true)][string]$Apk)
if (-not $env:ANDROID_HOME) { Write-Error 'ANDROID_HOME is not set'; exit 2 }
$bt = Get-ChildItem (Join-Path $env:ANDROID_HOME 'build-tools') | Sort-Object { [version]($_.Name -replace '-.*$', '') } | Select-Object -Last 1
& (Join-Path $bt.FullName 'apksigner.bat') verify --print-certs -v $Apk
if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }
& (Join-Path $bt.FullName 'aapt2.exe') dump badging $Apk | Select-Object -First 1
