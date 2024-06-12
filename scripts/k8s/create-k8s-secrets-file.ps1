# Functions to encode and decode Base64
Function ConvertFrom-Base64($base64) {
    return [System.Text.Encoding]::ASCII.GetString([System.Convert]::FromBase64String($base64))
}

Function ConvertTo-Base64($plain) {
    return [Convert]::ToBase64String([Text.Encoding]::ASCII.GetBytes($plain))
}

# Prompt user for secrets
$username = Read-Host "Enter your database username"
$password = Read-Host "Enter your database password"
$jdbcUrl = Read-Host "Enter your JDBC URL"
$jwtSecret = Read-Host "Enter your JWT secret"
$jwtCookieSameSite = Read-Host "Enter your JWT cookie same site"

# Encode secrets in Base64
$encodedUsername = ConvertTo-Base64 $username
$encodedPassword = ConvertTo-Base64 $password
$encodedJdbcUrl = ConvertTo-Base64 $jdbcUrl
$encodedJwtSecret = ConvertTo-Base64 $jwtSecret
$encodedJwtCookieSameSite = ConvertTo-Base64 $jwtCookieSameSite

# Read the template file
$templateFilePath = "k8s-secrets.yml"
$templateContent = Get-Content $templateFilePath

# Replace placeholders with Base64 encoded secrets
$templateContent = $templateContent -replace "<base64-encoded-username>", $encodedUsername
$templateContent = $templateContent -replace "<base64-encoded-password>", $encodedPassword
$templateContent = $templateContent -replace "<base64-encoded-jdbc-url>", $encodedJdbcUrl
$templateContent = $templateContent -replace "<base64-encoded-jwt-secret>", $encodedJwtSecret
$templateContent = $templateContent -replace "<base64-encoded-jwt-cookie-same-site>", $encodedJwtCookieSameSite

# Write the updated content back to the template file
$templateContent | Set-Content $templateFilePath

Write-Output "Secrets have been encoded and the template file has been updated."
