# SPRING MICROSERVICES

## TECH STACK

- SPRING BOOT 3.1.5
- MYSQL 8.0.32
- SPRING SECURITY 6.1.5
- LOGSTASH 8
- ELASTICSEARCH 8
- KIBANA 8
- ZIPKIN 2.24
- RABBITMQ 3-management-alpine
- DOCKER


## ELK stack

In windows download from the [page](https://www.elastic.co/) official files and then run:

```bash
  #START
  cp logstash.conf LOGSTASH_FOLDER
  cd LOGSTASH_FOLDER
  bin\logstash -f logstash.conf

  #START
  cd ELASTIC_FOLDER
  bin\elasticsearch.bat 
  
  #START
  cd KIBANA_FOLDER
  bin\kibana.bat
  
  #RENEW ELASTIC TOKEN FOR KIBANA
  bin/elasticsearch-create-enrollment-token -s kibana
```

## Config properties

```url
https://github.com/pblgllgs/config-server-j17-sb3
```

## K8S