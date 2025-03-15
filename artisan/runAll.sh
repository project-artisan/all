#!/bin/bash

LOGFILE="error.log"
DATE="2024-02-01"

# 102부터 115까지 반복
for id in {102..115}; do
	if [ "$id" -eq 104 ]; then
		echo "Skipping ID $id"
		continue
	fi
	if [ "$id" -eq 111 ]; then
  		echo "Skipping ID $id"
  		continue
  	fi
	if [ "$id" -eq 114 ]; then
		echo "Skipping ID $id"
		continue
	fi


	echo "Processing ID: $id"

  # 첫 번째 배치 실행
  sh batch.sh "$id" "$DATE"
  if [ $? -ne 0 ]; then
	  echo "[$(date)] Error: batch.sh $id $DATE failed." >> "$LOGFILE"
	  continue
  fi

  # 두 번째 배치 실행 (MigrationJob 인자 포함)
  sh batch.sh "$id" "$DATE" MigrationJob
  if [ $? -ne 0 ]; then
	  echo "[$(date)] Error: batch.sh $id $DATE MigrationJob failed." >> "$LOGFILE"
	  continue
  fi

  echo "ID $id processed successfully."
done


