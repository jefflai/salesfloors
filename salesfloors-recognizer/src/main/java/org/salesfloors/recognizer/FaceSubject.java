package org.salesfloors.recognizer;

public enum FaceSubject {
	JEFF("1224055@facebook.com"),
	DEREK("517172868@facebook.com"),	
	ELAINA("730592586@facebook.com")
	;
	
	private String fbId;
	
	private FaceSubject(String fbId) {
		this.fbId = fbId;
	}

	public String getFbId() {
		return fbId;
	}	
}
