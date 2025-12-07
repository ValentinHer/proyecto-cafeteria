package com.valentin.reservacion_citas.domain.service.implementation;

import com.valentin.reservacion_citas.domain.service.MessageService;
import com.valentin.reservacion_citas.domain.service.RoleService;
import com.valentin.reservacion_citas.persistence.entity.Message;
import com.valentin.reservacion_citas.persistence.entity.Role;
import com.valentin.reservacion_citas.persistence.entity.User;
import com.valentin.reservacion_citas.persistence.repository.MessageRepository;
import com.valentin.reservacion_citas.persistence.repository.UserRepository;
import com.valentin.reservacion_citas.web.dto.response.MessageResDto;
import com.valentin.reservacion_citas.web.dto.response.MsgDataResDto;
import com.valentin.reservacion_citas.web.dto.response.MsgStatus;
import com.valentin.reservacion_citas.web.exception.NotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageServiceImpl implements MessageService {
    private final RoleService roleService;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private static final Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);

    public MessageServiceImpl(RoleService roleService, UserRepository userRepository, MessageRepository messageRepository) {
        this.roleService = roleService;
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
    }

    @Override
    public MessageResDto saveMessage(Message message) {
        Role roleFound = roleService.findByName("ADMIN");
        Optional<User> ownerFound = userRepository.findByRoleId(roleFound.getId());

        if (ownerFound.isPresent()) {
            Message newMessage = new Message();
            newMessage.setMessage(message.getMessage());
            newMessage.setUserId(ownerFound.get().getId());

            messageRepository.save(newMessage);
            return new MessageResDto("Se guardó correctament", HttpStatus.OK.value());
        }

        logger.error("El usuario admin no se encontró");
        throw new NotFoundException("No se pudo guardar el email");
    }

    @Transactional
    @Override
    public MsgDataResDto getMessagesByUser(String email) {
        Optional<User> userFound = userRepository.findByEmail(email);

        if (userFound.isPresent()) {
            List<Message> messages = messageRepository.findAllByUserId(userFound.get().getId());

            MsgDataResDto newMessage = new MsgDataResDto();
            newMessage.setMessage("Mensajes recuperados");
            newMessage.setStatus(MsgStatus.SUCCESS);
            newMessage.setData(messages);

            return newMessage;
        }

        throw new NotFoundException("No se pudieron recuperar los mensajes del usuario");
    }
}
