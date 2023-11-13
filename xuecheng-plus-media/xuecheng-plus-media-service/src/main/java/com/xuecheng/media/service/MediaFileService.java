package com.xuecheng.media.service;

import com.xuecheng.base.modle.PageParams;
import com.xuecheng.base.modle.PageResult;
import com.xuecheng.media.model.dto.QueryMediaParamsDto;
import com.xuecheng.media.model.dto.UploadFileParamsDto;
import com.xuecheng.media.model.dto.UploadFileResultDto;
import com.xuecheng.media.model.po.MediaFiles;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @description 媒资文件管理业务类
 * @author Mr.M
 * @date 2022/9/10 8:55
 * @version 1.0
 */
public interface MediaFileService {

 /**
  * @description 媒资文件查询方法
  * @param pageParams 分页参数
  * @param queryMediaParamsDto 查询条件
  * @return com.xuecheng.base.model.PageResult<com.xuecheng.media.model.po.MediaFiles>
  * @author Mr.M
  * @date 2022/9/10 8:57
 */
 public PageResult<MediaFiles> queryMediaFiels(Long companyId, PageParams pageParams, QueryMediaParamsDto queryMediaParamsDto);

 /**
  * 上传文件接口
  * @param companyId 机构id
  * @param uploadFileParamsDto 文件的信息
  * @param localFilePath 文件的本地路径
  * @return
  */
 public UploadFileResultDto updatefile(Long companyId, UploadFileParamsDto uploadFileParamsDto, String localFilePath);

 /**
  *
  * @param companyId 机构id
  * @param md5 文件md5值
  * @param uploadFileParamsDto 上传文件的信息
  * @param bucket_mediafiles 桶
  * @param objectName 对象名称
  * @return
  */
 MediaFiles addMediaFilesToDb(Long companyId, String md5, UploadFileParamsDto uploadFileParamsDto, String bucket_mediafiles, String objectName);

}
