
  使用说明：


  // 1、初始化
  ImageLoader imageLoader=new ImageLoader(new GlideImageLoaderDelegate());

  // 2、加载图片
  imageLoader.loadImage(context,
                GlideImageConfig
                        .builder()
                        .url(imgurl)
                        .imageView(imageView)
                        .transformation(mTranformation)
                        .cacheStrategy(DiskCacheStrategy.XX)
                        .errorPic(errpic)
                        .isClearDiskCache(true)
                       /* ... 根据需求调用*/
                        .build());


   // 3、清除某一image的缓存
   imageLoader.clear(context, GlideImageConfig.builder()
                      .imageViews(imageView)
                      .build());

   // 4、加载进度
   GlideProgressManager.getInstance().addResponseListener(url/*请求的URL*/, listener);
