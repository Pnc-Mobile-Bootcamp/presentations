# Block 1.1 and 2.3: look inside the built APKs without any extra tools.
# Usage (PowerShell, from the app project root after building):  .\inspect_apk.ps1 -Project D:\BootcampMobile\Tests\JetpackComposeDemosPrep
param([string]$Project = "D:\BootcampMobile\Tests\JetpackComposeDemosPrep")

$sdk = "$env:LOCALAPPDATA\Android\Sdk\build-tools"
$aapt2 = Get-ChildItem $sdk -Recurse -Filter aapt2.exe | Sort-Object FullName -Descending | Select-Object -First 1 -ExpandProperty FullName
$apks = @{
  debug   = "$Project\app\build\outputs\apk\debug\app-debug.apk"
  release = "$Project\app\build\outputs\apk\release\app-release-unsigned.apk"
}
foreach ($k in $apks.Keys) {
  $apk = $apks[$k]
  if (-not (Test-Path $apk)) { Write-Host "[$k] not built: $apk"; continue }
  Write-Host "`n=== $k : $apk"
  Write-Host "Manifest flags:"
  & $aapt2 dump xmltree --file AndroidManifest.xml $apk | Select-String "allowBackup|networkSecurityConfig|debuggable" | ForEach-Object { Write-Host ("  " + $_.Line.Trim()) }

  # Strings and class names live in classes*.dex inside the zip. Unzip to a temp folder and search the raw bytes.
  $tmp = Join-Path $env:TEMP "apk_$k"
  Remove-Item $tmp -Recurse -Force -ErrorAction SilentlyContinue
  Add-Type -AssemblyName System.IO.Compression.FileSystem
  [IO.Compression.ZipFile]::ExtractToDirectory($apk, $tmp)
  Write-Host "Searching DEX files for interesting strings:"
  foreach ($needle in "kazoopromotions.com", "bootcampcentral.com", "Good for 1 free pizza", "FakeAuthRepository", "LoginUseCase") {
    $hits = Get-ChildItem $tmp -Filter "classes*.dex" | Where-Object { [Text.Encoding]::GetEncoding(28591).GetString([IO.File]::ReadAllBytes($_.FullName)).Contains($needle) }
    "{0,-26} found in {1} dex file(s)" -f $needle, @($hits).Count
  }
}
Write-Host "`nTip: for the visual version open the APK in Android Studio (Build > Analyze APK) and browse classes.dex."
