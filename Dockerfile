FROM openjdk:11-jre
EXPOSE 9000
COPY app /app
CMD /app/bin/start 
