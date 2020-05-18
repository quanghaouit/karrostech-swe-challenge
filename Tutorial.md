## NOTE
- Please access file `application.properties` and change `file.upload-dir` as exist location in your system.
- I design header `user-name` as a access-token for those endpoint. So please set header user-name for each request.

## List API endpoint
- Upload File API
    + curl --header "user-name:quanghao" --form file=@localfilename --form press=OK localhost:8080/uploadFile
- Load GPS Page API
    + curl --header "user-name:quanghao" localhost:8080/gps?page=0&size=2&sort=uploadDateTime,desc
- Get GPS Detail API
    + curl --header "user-name:quanghao" localhost:8080/gps/1
- Load GPS resource file
    + curl --header "user-name:quanghao" http://localhost:8080/downloadFile/quanghao/sample.gpx