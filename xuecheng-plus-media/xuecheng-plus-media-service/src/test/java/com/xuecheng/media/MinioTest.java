package com.xuecheng.media;

import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil;
import io.minio.*;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author rosemaryxxxxx
 * @Date 2023/11/8 15:13
 * @PackageName:com.xuecheng.media
 * @ClassName: MinioTest
 * @Description: TODO
 * @Version 1.0
 */
public class MinioTest {

    //客户端
    static MinioClient minioClient =
            MinioClient.builder()
                    .endpoint("http://192.168.101.65:9000")
                    .credentials("minioadmin", "minioadmin")
                    .build();


    //上传文件
    @Test
    public void upload() {

        //通过扩展名拿到媒体资源的mimeType
        ContentInfo extensionMatch = ContentInfoUtil.findExtensionMatch(".mp4"); //video/mp4
        String mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE; //通用mineType,字节流
        if(extensionMatch != null){
            mimeType = extensionMatch.getMimeType();
        }


        try {
            //上传文件参数信息
            UploadObjectArgs testbucket = UploadObjectArgs.builder()
                    .bucket("testbucket")
                    .filename("D:\\data\\xcdev\\upload\\video1.mp4")
//                    .object("video1.mp4")//添加在根目录
                    .object("1/2/video1.mp4")//添加在子目录
                    .contentType(mimeType)//默认根据扩展名确定文件内容类型，也可以指定
                    .build();
            minioClient.uploadObject(testbucket);
            System.out.println("上传成功");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("上传失败");
        }

    }

    //删除文件
    @Test
    public void delete() {
        try {
            //删除文件参数信息
            RemoveObjectArgs removeObjectArgs = RemoveObjectArgs.builder().bucket("testbucket").object("1/2/video1.mp4").build();
            minioClient.removeObject(removeObjectArgs);
            System.out.println("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("删除失败");
        }

    }

    //查询文件 从minio中下载
    @Test
    public void getFile() {
        GetObjectArgs getObjectArgs = GetObjectArgs.builder().bucket("testbucket").object("1/2/video1.mp4").build();
        try(
                FilterInputStream inputStream = minioClient.getObject(getObjectArgs);
                FileOutputStream outputStream = new FileOutputStream(new File("D:\\data\\xcdev\\download\\1_1.mp4"));
        ) {
            IOUtils.copy(inputStream,outputStream);

            //校验文件的完整性对文件的内容进行md5
            FileInputStream fileInputStream1 = new FileInputStream(new File("D:\\data\\xcdev\\upload\\video1.mp4"));
            String source_md5 = DigestUtils.md5Hex(fileInputStream1);
            FileInputStream fileInputStream = new FileInputStream(new File("D:\\data\\xcdev\\download\\1_1.mp4"));
            String local_md5 = DigestUtils.md5Hex(fileInputStream);
            if(source_md5.equals(local_md5)){
                System.out.println("下载成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //将分块文件上传至minio
    @Test
    public void uploadChunk(){
        String chunkFolderPath = "D:\\data\\xcdev\\upload\\chunk\\";
        File chunkFolder = new File(chunkFolderPath);
        //分块文件
        File[] files = chunkFolder.listFiles();
        //将分块文件上传至minio
        for (int i = 0; i < files.length; i++) {
            try {
                UploadObjectArgs uploadObjectArgs = UploadObjectArgs.builder().bucket("testbucket").object("chunk/" + i).filename(files[i].getAbsolutePath()).build();
                minioClient.uploadObject(uploadObjectArgs);
                System.out.println("上传分块成功"+i);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    //合并文件，要求分块文件最小5M
    @Test
    public void test_merge() throws Exception {
        List<ComposeSource> sources = Stream.iterate(0, i -> ++i)
                .limit(5)
                .map(i -> ComposeSource.builder()
                        .bucket("testbucket")
                        .object("chunk/".concat(Integer.toString(i)))
                        .build())
                .collect(Collectors.toList());

        ComposeObjectArgs composeObjectArgs = ComposeObjectArgs.builder().bucket("testbucket").object("merge01.mp4").sources(sources).build();
        minioClient.composeObject(composeObjectArgs);

    }
    //清除分块文件
    @Test
    public void test_removeObjects(){
        //合并分块完成将分块文件清除
        List<DeleteObject> deleteObjects = Stream.iterate(0, i -> ++i)
                .limit(5)
                .map(i -> new DeleteObject("chunk/".concat(Integer.toString(i))))
                .collect(Collectors.toList());

        RemoveObjectsArgs removeObjectsArgs = RemoveObjectsArgs.builder().bucket("testbucket").objects(deleteObjects).build();
        Iterable<Result<DeleteError>> results = minioClient.removeObjects(removeObjectsArgs);
        results.forEach(r->{
            DeleteError deleteError = null;
            try {
                deleteError = r.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }




}
