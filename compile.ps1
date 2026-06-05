$root = Split-Path -Parent $MyInvocation.MyCommand.Path
$src = Join-Path $root "src"
$lib = Join-Path $root "lib"
$out = Join-Path $root "out"

$jar = Get-ChildItem -LiteralPath $lib -Filter "*.jar" | Select-Object -First 1

if (-not (Test-Path -LiteralPath $out)) {
    New-Item -ItemType Directory -Path $out -Force | Out-Null
}

$files = Get-ChildItem -Recurse -LiteralPath $src -Filter "*.java" | ForEach-Object { $_.FullName }

if ($jar) {
    javac -cp "$($jar.FullName)" -d $out $files
} else {
    javac -d $out $files
}

if ($?) {
    Write-Host "Compilation successful!" -ForegroundColor Green
} else {
    Write-Host "Compilation failed!" -ForegroundColor Red
}
