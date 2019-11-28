package jp.profield.chat.service.impl;

import java.util.Optional;

import jp.profield.chat.model.Attachment;
import jp.profield.chat.models.repository.AttachemntRepository;
import jp.profield.chat.service.AttachmentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AttachmentServiceImpl implements AttachmentService{
	

	@Autowired
	AttachemntRepository attachemntRepository;

	@Override
	public Attachment saveAttachment(Attachment attachment) {
		return attachemntRepository.save(attachment);
	}

	@Override
	public Optional<Attachment> findOneByid(String attachmentId) {
		
		return attachemntRepository.findById(attachmentId);
	}
}
