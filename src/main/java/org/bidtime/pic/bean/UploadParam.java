package org.bidtime.pic.bean;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class UploadParam {

	private String name;
	
	private String content_type;
	
	private String path;
	
	private String md5;
		
	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	private Integer size;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent_type() {
		return content_type;
	}

	public void setContent_type(String content_type) {
		this.content_type = content_type;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("name", this.name)
				.append("content_type", this.content_type)
				.append("path", this.path)
				.append("md5", this.md5)
				.append("size", this.size)
				.toString();
	}
	
//	public String mergeArchvUrl() {
//		return GlobalConst.getInstance().mergeArchvUrl(path, name);
//	}
	
	private static final String PROP_NAME = "name";
	private static final String CONTENT_TYPE = "content_type";
	private static final String PROP_PATH = "path";
	private static final String PROP_MD5 = "md5";
	private static final String PROP_SIZE = "size";
	
	public void setPropValue(String propName, String propValue) {
		if (propName.equals(PROP_NAME)) {
			setName(propValue);
		} else if (propName.equals(CONTENT_TYPE)) {
			setContent_type(propValue);					
		} else if (propName.equals(PROP_PATH)) {
			setPath(propValue);
		} else if (propName.equals(PROP_MD5)) {
			setMd5(propValue);
		} else if (propName.equals(PROP_SIZE)) {
			setSize(Integer.parseInt(propValue));
		}
	}

	/**
	 * @see java.lang.Object#equals(Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof UploadParam)) {
			return false;
		}
		UploadParam rhs = (UploadParam) object;
		return new EqualsBuilder().appendSuper(super.equals(object)).append(
				this.content_type, rhs.content_type)
				.append(this.size, rhs.size).append(this.path, rhs.path)
				.append(this.md5, rhs.md5).append(this.name, rhs.name)
				.isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder(-404694209, 2059458549).appendSuper(
				super.hashCode()).append(this.content_type).append(this.size)
				.append(this.path).append(this.md5).append(this.name)
				.toHashCode();
	}
		
}
