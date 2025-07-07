#!/bin/bash

# Base URL of your backend
BASE_URL="http://localhost:8080"
USERNAME="testuser"
PASSWORD="password"
EMAIL="prestonbeachum@gmail.com"

echo " Registering test user..."
curl -s -X POST $BASE_URL/register \
-H "Content-Type: application/json" \
-d "{\"username\":\"$USERNAME\", \"password\":\"$PASSWORD\", \"email\":\"$EMAIL\"}" \
|| echo "️ User may already exist."

# Create a reminder time 2 minutes from now
REMINDER_TIME=$(date -v+2M +"%Y-%m-%dT%H:%M:%S")
echo " Creating test task with reminderTime: $REMINDER_TIME"

curl -s -u $USERNAME:$PASSWORD -X POST $BASE_URL/tasks \
-H "Content-Type: application/json" \
-d "{
  \"title\": \"Test Reminder Task\",
  \"description\": \"Verify reminder system\",
  \"dueDate\": \"2025-07-07T23:59:00\",
  \"reminderTime\": \"$REMINDER_TIME\"
}" \
&& echo " Test task created."

echo " Fetching tasks for user $USERNAME:"
curl -s -u $USERNAME:$PASSWORD $BASE_URL/tasks | jq

