package com.valentin.reservacion_citas.web.controller;

import com.valentin.reservacion_citas.domain.service.MessageService;
import com.valentin.reservacion_citas.web.dto.response.MsgDataResDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/messages")
public class MessageController {
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping
    public ResponseEntity<MsgDataResDto> getMessagesByUser(Principal principal) {
        return new ResponseEntity<>(messageService.getMessagesByUser(principal.getName()), HttpStatus.OK);
    }
}
