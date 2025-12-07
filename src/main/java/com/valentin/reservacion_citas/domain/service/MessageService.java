package com.valentin.reservacion_citas.domain.service;

import com.valentin.reservacion_citas.persistence.entity.Message;
import com.valentin.reservacion_citas.web.dto.response.MessageResDto;
import com.valentin.reservacion_citas.web.dto.response.MsgDataResDto;

public interface MessageService {
    MessageResDto saveMessage(Message message);

    MsgDataResDto<Message> getMessagesByUser(String email);
}
