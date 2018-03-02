package org.bidtime.pic.bean;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class FileParam {

	private String name;
	
	private String content_type;
	
	private String path;
	
	private String md5;

	private Integer size;
	
	private String url;
	
	private String index;

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

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
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("name", this.name)
				.append("content_type", this.content_type)
				.append("path", this.path)
				.append("md5", this.md5)
				.append("size", this.size)
				.append("url", this.url)
				.append("index", this.index)
				.toString();
	}
	
	/**
	 * @see java.lang.Object#equals(Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof FileParam)) {
			return false;
		}
		FileParam rhs = (FileParam) object;
		return new EqualsBuilder().appendSuper(super.equals(object)).append(
				this.content_type, rhs.content_type)
				.append(this.size, rhs.size).append(this.path, rhs.path)
				.append(this.md5, rhs.md5).append(this.name, rhs.name)
				.append(this.url, rhs.url).append(this.index, rhs.index)
				.isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder(-404694209, 2059458549).appendSuper(
				super.hashCode()).append(this.content_type).append(this.size)
				.append(this.path).append(this.md5).append(this.name).append(this.url)
				.append(this.index).toHashCode();
	}
		
}
