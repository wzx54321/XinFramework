上传文件示例：

                File file=new File(path);

                PostRequest<String> postRequest = OkGo.<String>post(url)//
                        .headers("aaa", "111")//
                        .params("bbb", "222")//
                        .params("fileKey" + i, file)//
                        .converter(new StringConvert());

                UploadTask<String> task = OkUpload.request(path+file.getPath(), postRequest)//
                        .priority(random.nextInt(100))//
                        .extra1(imageItem)//
                        .save();