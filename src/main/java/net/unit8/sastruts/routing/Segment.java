package net.unit8.sastruts.routing;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.seasar.framework.util.StringUtil;
import org.seasar.struts.util.URLEncoderUtil;

public abstract class Segment {
	public static final String RESERVED_PCHAR = ":@&=+$,;";

	private String value;
	private boolean isOptional;

	public Segment() {
		this(null);
	}
	public Segment(String value) {
		this.value = value;
		isOptional = false;
	}

	public int numberOfCaptures() {
		return Pattern.compile(regexpChunk()).matcher("").groupCount();
	}

	public String getExtractionCode() {
		return null;
	}

	public String continueStringStructure(List<Segment> list, Options hash) {
		if (list.isEmpty()) {
			return interpolationStatement(list, hash);
		} else {
			List<Segment> newPriors = list.subList(0, list.size() - 1);
			return list.get(list.size() - 1).stringStructure(newPriors, hash);
		}
	}
	public String interpolationChunk(Options hash) {
		return URLEncoderUtil.encode(value);
	}

	public String interpolationStatement(List<Segment> list, Options hash) {
		StringBuilder chunks = new StringBuilder(128);
		for(Segment seg : list) {
			chunks.append(seg.interpolationChunk(hash));
		}
		chunks.append(interpolationChunk(hash));
		return allOptionalsAvailableCondition(list, hash) ? chunks.toString() : "";
	}

	public String stringStructure(List<Segment> list, Options hash) {
		return isOptional ? continueStringStructure(list, hash) : interpolationStatement(list, hash);
	}

	public boolean allOptionalsAvailableCondition(List<Segment> priorSegments, Options hash) {
		for (Segment segment : priorSegments) {
			if (!segment.isOptional() && segment.hasKey() && StringUtil.isEmpty(hash.getString(segment.getKey())))
				return  false;
		}
		return true;
	}

	public void matchExtraction(Options params, Matcher match, int nextCapture) {
	}

	public boolean hasKey() {
		return false;
	}

	public String getKey() {
		return null;
	}

	public boolean hasDefault() {
		return false;
	}

	public String getDefault() {
		return null;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public abstract String regexpChunk();
	public boolean isOptional() {
		return isOptional;
	}

	public void setOptional(boolean optional) {
		this.isOptional = optional;
	}

	public void setRegexp(Pattern regexp) {}
	public void setDefault(String def) {}

	public String buildPattern(String pattern) {
		return null;
	}

	public String getRegexp() {
		return null;
	}
}
