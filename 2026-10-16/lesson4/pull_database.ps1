# Block 1.4b and 2.3: show that the app's Room database is plaintext on the device (debug builds only; run-as needs debuggable).
# Start an emulator, install and run the app, add a ToDo, then:   .\pull_database.ps1
param([string]$Out = "$env:TEMP\app_database_pull")

$adb = "$env:LOCALAPPDATA\Android\Sdk\platform-tools\adb.exe"
$pkg = "com.pnc.jetpackcomposedemosprep"
New-Item -ItemType Directory -Force $Out | Out-Null

& $adb devices
Write-Host "`nFiles in the app's private databases folder:"
& $adb shell run-as $pkg ls -l databases
foreach ($f in "app_database", "app_database-wal", "app_database-shm") {
  & $adb exec-out run-as $pkg cat "databases/$f" > (Join-Path $Out $f)
}
Write-Host "`nPulled to $Out"
Write-Host "Readable strings from the main file (no tools needed, just text):"
$bytes = [IO.File]::ReadAllBytes((Join-Path $Out "app_database"))
[Text.Encoding]::GetEncoding(28591).GetString($bytes) -split "[^\x20-\x7E]{2,}" | Where-Object { $_.Length -gt 6 } | Select-Object -First 25
Write-Host "`nNo key, no password, no root needed beyond a debuggable app. Android Studio's App Inspection > Database Inspector shows the same tables live."
