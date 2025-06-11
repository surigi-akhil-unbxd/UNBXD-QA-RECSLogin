#!/bin/bash

# Create the driver directory if it doesn't exist
mkdir -p src/main/resources/driver

# Download ChromeDriver 135
curl -L -o chromedriver.zip "https://edgedl.me.gvt1.com/edgedl/chrome/chrome-for-testing/135.0.7049.115/mac-x64/chromedriver-mac-x64.zip"

# Extract ChromeDriver
unzip -o chromedriver.zip

# Move ChromeDriver to the resources directory
mv chromedriver-mac-x64/chromedriver src/main/resources/driver/
chmod +x src/main/resources/driver/chromedriver

# Clean up
rm -rf chromedriver.zip chromedriver-mac-x64

echo "ChromeDriver 135.0.7049.115 has been downloaded and installed successfully!" 