package Capstone.easyreservation.exception;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorsPayload {

	private String message;
	private Date timestamp;
	private int internalCode;

}