# URL Shortener API Test Script

Write-Host "Starting URL Shortener Service..." -ForegroundColor Green
Start-Job -ScriptBlock { 
    Set-Location "c:\Users\v.kazlou\copilot-workshop-1-2-main\url-shortener-service-homework"
    mvn spring-boot:run 
} | Out-Null

Write-Host "Waiting for service to start..." -ForegroundColor Yellow
Start-Sleep -Seconds 20

Write-Host "`n1. Testing POST /api/urls (Create short URL)..." -ForegroundColor Cyan
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/urls" `
        -Method POST `
        -Headers @{"Content-Type"="application/json"} `
        -Body '{"originalUrl":"https://example.com/very/long/url"}'
    
    Write-Host "✓ Success! Short URL created:" -ForegroundColor Green
    Write-Host "  Short Key: $($response.shortKey)"
    Write-Host "  Short URL: $($response.shortUrl)"
    Write-Host "  Original URL: $($response.originalUrl)"
    
    $shortKey = $response.shortKey
} catch {
    Write-Host "✗ Failed: $_" -ForegroundColor Red
    exit 1
}

Write-Host "`n2. Testing GET /{shortKey} (Redirect)..." -ForegroundColor Cyan
try {
    $response = Invoke-WebRequest -Uri "http://localhost:8080/$shortKey" `
        -MaximumRedirection 0 `
        -ErrorAction SilentlyContinue
    
    if ($response.StatusCode -eq 302) {
        Write-Host "✓ Success! Redirect response (302 Found)" -ForegroundColor Green
        Write-Host "  Location: $($response.Headers.Location)"
    }
} catch {
    Write-Host "✗ Failed: $_" -ForegroundColor Red
}

Write-Host "`n3. Testing GET /api/urls/{shortKey} (Stats)..." -ForegroundColor Cyan
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/urls/$shortKey" `
        -Method GET
    
    Write-Host "✓ Success! Stats retrieved:" -ForegroundColor Green
    Write-Host "  Short Key: $($response.shortKey)"
    Write-Host "  Visit Count: $($response.visitCount)"
    Write-Host "  Created At: $($response.createdAt)"
} catch {
    Write-Host "✗ Failed: $_" -ForegroundColor Red
}

Write-Host "`n4. Testing 404 (Not Found)..." -ForegroundColor Cyan
try {
    Invoke-RestMethod -Uri "http://localhost:8080/notexist" `
        -Method GET `
        -ErrorAction Stop
    Write-Host "✗ Should have returned 404" -ForegroundColor Red
} catch {
    if ($_.Exception.Response.StatusCode -eq 404) {
        Write-Host "✓ Success! 404 Not Found returned correctly" -ForegroundColor Green
    } else {
        Write-Host "✗ Wrong status code: $($_.Exception.Response.StatusCode)" -ForegroundColor Red
    }
}

Write-Host "`n5. Testing 400 (Bad Request - empty URL)..." -ForegroundColor Cyan
try {
    Invoke-RestMethod -Uri "http://localhost:8080/api/urls" `
        -Method POST `
        -Headers @{"Content-Type"="application/json"} `
        -Body '{"originalUrl":""}' `
        -ErrorAction Stop
    Write-Host "✗ Should have returned 400" -ForegroundColor Red
} catch {
    if ($_.Exception.Response.StatusCode -eq 400) {
        Write-Host "✓ Success! 400 Bad Request returned correctly" -ForegroundColor Green
    } else {
        Write-Host "✗ Wrong status code: $($_.Exception.Response.StatusCode)" -ForegroundColor Red
    }
}

Write-Host "`nStopping service..." -ForegroundColor Yellow
Get-Job | Stop-Job
Get-Job | Remove-Job

Write-Host "`nAll tests completed!" -ForegroundColor Green
