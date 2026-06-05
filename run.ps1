$root = Split-Path -Parent $MyInvocation.MyCommand.Path
$lib = Join-Path $root "lib"
$out = Join-Path $root "out"

$jar = Get-ChildItem -LiteralPath $lib -Filter "*.jar" | Select-Object -First 1

if ($jar) {
    java -cp "$out;$($jar.FullName);$root\database" com.hotel.ui.HotelManagementSystem
} else {
    java -cp "$out;$root\database" com.hotel.ui.HotelManagementSystem
}
