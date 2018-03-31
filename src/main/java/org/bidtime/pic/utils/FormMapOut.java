package org.bidtime.pic.utils;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bidtime.pic.bean.FileParam;
import org.bidtime.pic.bean.FilePicVO;
import org.bidtime.pic.bean.UploadParam;
import org.bidtime.pic.fdfs.UploadFdfs;
import org.bidtime.pic.params.GlobalConst;

public class FormMapOut {
	
    //private static final Logger log = LoggerFactory.getLogger(HtmlForm2.class);
	
    @SuppressWarnings("unchecked")
    public static ResultDTO<List<?>> parserToDTO(Map<String, UploadParam> mapParm) throws Exception {
		if (mapParm.isEmpty()) {
			return ResultDTO.error("无上传文件");
		} else {
			return new ResultDTO<>(mapToList(mapParm));
		}
  	}
	
	private static List<?> mapToList(Map<String, UploadParam> map) throws Exception {
		if (GlobalConst.getInstance().getFdfs()) {
			return mapToListFdfs(map);
		} else {
			if (GlobalConst.getInstance().getOutPattern()==1) {
				return mapToListOutParam(map);
			} else {
				return mapToListPicVO(map);
			}
		}
	}
	
	private static List<FilePicVO> mapToListPicVO(Map<String, UploadParam> map) {
		List<FilePicVO> list = new LinkedList<>();
		for (Map.Entry<String, UploadParam> entry : map.entrySet()) {
			FilePicVO bean = new FilePicVO(entry.getKey(), mv_mergeArchvUrl(entry.getValue()));
			//
			list.add(bean);
		}
		return list;
	}
	
	private static List<FileParam> mapToListOutParam(Map<String, UploadParam> map) throws Exception {
		List<FileParam> list = new LinkedList<>();
		for (Map.Entry<String, UploadParam> entry : map.entrySet()) {
			UploadParam up = entry.getValue();
			//OutParam param = BeanUtils.copyProps(entry.getValue(), OutParam.class);
			FileParam param = new FileParam();
			param.setIndex(entry.getKey());
			param.setContent_type(up.getContent_type());
			param.setMd5(up.getMd5());
			param.setName(up.getName());
			param.setSize(up.getSize());
			//
			String url = mv_mergeArchvUrl(up);
			param.setUrl(url);
			//
			list.add(param);
		}
		return list;
	}
	
	private static List<FileParam> mapToListFdfs(Map<String, UploadParam> map) throws Exception {
		List<FileParam> list = new LinkedList<>();
		for (Map.Entry<String, UploadParam> entry : map.entrySet()) {
			UploadParam up = entry.getValue();
			FileParam param = new FileParam();
			param.setIndex(entry.getKey());
			param.setContent_type(up.getContent_type());
			param.setMd5(up.getMd5());
			param.setName(up.getName());
			param.setSize(up.getSize());
			//
			String newFile = renameFile(up.getPath(), up.getName());
			String url = UploadFdfs.upload(newFile);	//mergeArchvUrl(up);
			param.setUrl(url);
			//
			list.add(param);
		}
		return list;
	}
	
	private static String mv_mergeArchvUrl(UploadParam bean) {
		String fileName = renameFile(bean.getPath(), bean.getName());
		return mergeArchvUrl(fileName);
	}
	
	private static String mergeArchvUrl(String fileName) {
		return GlobalConst.getInstance().mergeArchvUrl(fileName);
	}
	
	private static String renameFile(String path, String name) {
		return GlobalConst.getInstance().renameFile(path, name);
	}

}
