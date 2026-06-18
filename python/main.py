import os
from datetime import datetime
input_file = "server.log"
date_str = datetime.now().strftime("%Y-%m-%d")
output_file = f"security_alert_{date_str}.txt"

keywords = ["CRITICAL", "ERROR", "FAILED LOGIN"]

error_count = {
    "CRITICAL": 0,
    "ERROR": 0,
    "FAILED LOGIN": 0
}

filtered_lines = []

with open(input_file, "r") as file:
    for line in file:
        for key in keywords:
            if key in line:
                error_count[key] += 1
                filtered_lines.append(line)
                break 

with open(output_file, "w") as file:
    file.write("=== SECURITY ALERT REPORT ===\n")
    file.write(f"Generated on: {date_str}\n\n")

    for line in filtered_lines:
        file.write(line)

    file.write("\n=== SUMMARY ===\n")
    for key, value in error_count.items():
        file.write(f"{key}: {value}\n")

file_size = os.path.getsize(output_file)

print("Security alert file created successfully!")
print(" File Name: {output_file}")
print(" File Size: {file_size} bytes")
print("\n Error Summary:")
for key, value in error_count.items():
    print(f"{key}: {value}")
