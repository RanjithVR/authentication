package jp.profield.chat.service;

import java.util.Optional;

import jp.profield.chat.model.Attachment;

public interface AttachmentService {

	
	Attachment saveAttachment( Attachment attachment);
	
	Optional<Attachment> findOneByid(String attachmentId);
}
