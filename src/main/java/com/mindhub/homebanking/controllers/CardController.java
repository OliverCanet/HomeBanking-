package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.service.CardService;
import com.mindhub.homebanking.service.ClientService;
import com.mindhub.homebanking.utils.CardUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CardController {

    @Autowired
    private CardService cardService;

    @Autowired
    private ClientService clientService;




    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }


    @PostMapping(path = "/clients/current/cards")

    public ResponseEntity<Object> createCard(Authentication authentication, @RequestParam CardType type, @RequestParam CardColor color) {


        Client clientCurrent = clientService.findByEmail(authentication.getName());
        String cardNumber = CardUtils.getCardNumber();
        int cardCVV = getCardCVV();

        Set<Card> cardsActive = clientCurrent.getCards().stream().filter(card -> card.getActive().equals(true)).collect(Collectors.toSet());

        if(type.equals(null))
        {
            return new ResponseEntity<>("No type specified", HttpStatus.BAD_REQUEST);
        }
        if(color.equals(null))
        {
            return new ResponseEntity<>("No color specified", HttpStatus.BAD_REQUEST);
        }


        if (type.equals(CardType.CREDIT)) {
            if ((cardsActive.stream().filter(card -> card.getType().equals(CardType.CREDIT)).collect(Collectors.toSet()).size() >= 3)) {
                return new ResponseEntity<>("Max amount of Credit Cards", HttpStatus.FORBIDDEN);
            }

        }
        if (type.equals(CardType.DEBIT)) {
            if (cardsActive.stream().filter(card -> card.getType().equals(CardType.DEBIT)).collect(Collectors.toSet()).size() >= 3) {
                return new ResponseEntity<>("Max amount of Debit Cards", HttpStatus.FORBIDDEN);
            }

        }

            Card cardNew = new Card(clientCurrent.getFirstName() + " " + clientCurrent.getLastName(), type, color, cardNumber ,cardCVV, LocalDate.now().plusYears(5), LocalDate.now(),true);
            clientCurrent.addCard(cardNew);
            cardService.saveCard(cardNew);
            return new ResponseEntity<>(HttpStatus.CREATED);




    }
    @PatchMapping("/clients/current/cards/delete")
    public ResponseEntity<Object> deleteAccount(@RequestParam String number){
        Card deletedCard = cardService.findByNumber(number);
        deletedCard.setActive(false);
        cardService.saveCard(deletedCard);
        return new ResponseEntity<>("Card deleted", HttpStatus.OK);
    }

    public int getCardCVV() {
        int cardCVV = getRandomNumber(1000, 1000);
        return cardCVV;
    }

    public String getCardNumber() {
        String cardNumber = getRandomNumber(1000, 10000) + "-" + getRandomNumber(1000, 10000) + "-" + getRandomNumber(1000, 10000) + "-" + getRandomNumber(1000, 10000);
        return cardNumber;
    }
}



