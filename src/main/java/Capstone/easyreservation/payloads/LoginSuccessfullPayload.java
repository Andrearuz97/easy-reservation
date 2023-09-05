package Capstone.easyreservation.payloads;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoginSuccessfullPayload {
	String accessToken;
}