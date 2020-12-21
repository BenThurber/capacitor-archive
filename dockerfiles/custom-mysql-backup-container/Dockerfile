FROM databack/mysql-backup

# This ARG specifies a script that specifies the file name to use for the backup file
# See heading "Custom backup source file name" at https://hub.docker.com/r/databack/mysql-backup for more info
ARG FILENAME_SCRIPT

COPY $FILENAME_SCRIPT /scripts.d/target.sh

