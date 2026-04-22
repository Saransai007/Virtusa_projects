import os
from datetime import datetime

# Input log file
input_file = "server.log"

# Output file with date
date_str = datetime.now().strftime("%Y-%m-%d")
output_file = f"security_alert_{date_str}.txt"

# Keywords to search
keywords = ["CRITICAL", "ERROR", "FAILED LOGIN"]

# Dictionary to count occurrences
error_count = {
    "CRITICAL": 0,
    "ERROR": 0,
    "FAILED LOGIN": 0
}

filtered_lines = []

# 1. File Parsing + 2. Pattern Matching
with open(input_file, "r") as file:
    for line in file:
        for key in keywords:
            if key in line:
                error_count[key] += 1
                filtered_lines.append(line)
                break  # avoid double counting

# 4. Report Generation
with open(output_file, "w") as file:
    file.write("=== SECURITY ALERT REPORT ===\n")
    file.write(f"Generated on: {date_str}\n\n")

    for line in filtered_lines:
        file.write(line)

    file.write("\n=== SUMMARY ===\n")
    for key, value in error_count.items():
        file.write(f"{key}: {value}\n")

# 5. Automation (File Size)
file_size = os.path.getsize(output_file)

print("✅ Security alert file created successfully!")
print(f"📁 File Name: {output_file}")
print(f"📊 File Size: {file_size} bytes")

# Print summary in console
print("\n🔎 Error Summary:")
for key, value in error_count.items():
    print(f"{key}: {value}")