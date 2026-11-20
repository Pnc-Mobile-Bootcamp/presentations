# Fails (exit 1) when overall LINE coverage in a JaCoCo XML report is below -Min percent.
param(
    [Parameter(Mandatory = $true)][string]$Report,
    [Parameter(Mandatory = $true)][double]$Min
)
if (-not (Test-Path $Report)) {
    Write-Error "Coverage report not found: $Report"
    exit 2
}
$text = Get-Content -Raw $Report
# The report-level counters are the last LINE counter in the file
$all = [regex]::Matches($text, '<counter type="LINE" missed="(\d+)" covered="(\d+)"/>')
if ($all.Count -eq 0) {
    Write-Error "No LINE counter found in $Report"
    exit 2
}
$last = $all[$all.Count - 1]
$missed = [int]$last.Groups[1].Value
$covered = [int]$last.Groups[2].Value
$pct = [math]::Round(100.0 * $covered / ($missed + $covered), 2)
Write-Host "Line coverage: $pct% ($covered of $($missed + $covered) lines). Minimum: $Min%"
if ($pct -lt $Min) {
    Write-Host "Coverage gate FAILED"
    exit 1
}
Write-Host "Coverage gate passed"
