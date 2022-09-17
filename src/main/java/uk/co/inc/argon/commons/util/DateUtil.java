package uk.co.inc.argon.commons.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.ws.rs.core.Response.Status;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang3.StringUtils;

import uk.co.inc.argon.commons.exceptions.HttpException;

public class DateUtil {
    private static final String DATE_FORMAT = "dd-MMM-yyy";
    public static final String ADD_CLAIM_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    private DateFormat dateFormat;
	private DateFormat miliDateFormat;
	private DateFormat simpleDateFormat;
	private DateFormat displayDateFormat;
	private DateFormat nonStandardDisplayDateFormat;
	
	public DateUtil() {
		dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		miliDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		displayDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		nonStandardDisplayDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
	}

	public Date parseDate(String value) throws ParseException {
		if (value != null) {
			// Bug fix: TIA sends dates in the following format: 2014-09-03T09:02:00Z
			// This fix will replace the Z with +0200
			if (value.length() == 20 && value.toUpperCase().endsWith("Z")) {
				value = value.substring(0, 19);
				value += "+0200";
				return dateFormat.parse(value);
			}
			// End of fix

			if (value.length() == 10) {
				return simpleDateFormat.parse(value);
			}
			else if (value.length() == 24 && value.endsWith("Z")) {
				return miliDateFormat.parse(value);
			}
			else {
				return dateFormat.parse(value);
			}
		}
		return null;
	}

	/**
	 * Formats a Date object to <b>yyyy-MM-dd</b> string format.
	 *
	 * @param date
	 *            The date to be formatted
	 * @return the date as a formatted string or an empty ("") string.
	 */
	public String formatDate(Date date) {
		if (date != null) {
			return simpleDateFormat.format(date);
		}
		return "";
	}

	/**
	 * Formats a Date object to <b>yyyy-MM-dd'T'HH:mm:ssZ</b> string format.
	 *
	 * @param date
	 *            The date to be formatted
	 * @return the date as a formatted string or an empty ("") string.
	 */
	public String formatDateTime(Date date) {
		if (date != null) {
			return dateFormat.format(date);
		}
		return "";
	}
	
	public String formatDateTimeMilli(Timestamp ts) {
    	if(ts!=null){
    		   return ts.toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
    	}
    	return null;
    }

	/**
	 * Parses a date string from <b>yyyy-MM-dd'T'HH:mm:ssZ</b> to <b>yyyy-MM-dd HH:mm</b> string format.
	 *
	 * @param value
	 *            A date in <b>yyyy-MM-dd'T'HH:mm:ssZ</b> string format.
	 * @return The date in <b>yyyy-MM-dd HH:mm</b> string format.
	 * @throws ParseException
	 *             Thrown if the date cannot be parsed.
	 */
	public String convertToDisplayDateFormat(String value) throws ParseException {
		if (value != null) {
			Date date = dateFormat.parse(value);
			return displayDateFormat.format(date);
		}
		return value;
	}

	/**
	 * Takes a Date object and converts it to a String object whose format is <b>yyyy-MM-dd HH:mm</b>
	 *
	 * @param date
	 *            The date object to be converted
	 * @return The date in <b>yyyy-MM-dd HH:mm</b> string format.
	 */
	public String formatDisplayDateFormat(Date date) {
		if (date != null) {
			return displayDateFormat.format(date);
		}
		return "";
	}
	
	/**
	 * Takes a Date object and converts it to a String object whose format is <b>dd-MMM-yyy</b>
	 *
	 * @param date
	 *            The date object to be converted
	 * @return The date in <b>dd-MM-yyy</b> string format.
	 */
	public String formatNonStandardDisplayDateFormat(Date date) {
		if (date != null) {
			return nonStandardDisplayDateFormat.format(date);
		}
		return "";
	}

    public static XMLGregorianCalendar getDate(Date date) throws HttpException {
        
        try {
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(date);
         
            XMLGregorianCalendar xgc = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
            xgc.setTimezone(DatatypeConstants.FIELD_UNDEFINED);
            
            return xgc;
         } catch (DatatypeConfigurationException e) {
             e.printStackTrace();
             throw new HttpException(e.getMessage(),Status.INTERNAL_SERVER_ERROR.getStatusCode());
         }
    }

    public static XMLGregorianCalendar getDateTime() throws HttpException {
        
        try {
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(new Date());
         
            XMLGregorianCalendar xgc = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
            xgc.setTimezone(DatatypeConstants.FIELD_UNDEFINED);
            
            return xgc;
         } catch (DatatypeConfigurationException e) {
             e.printStackTrace();
             throw new HttpException(e.getMessage(),Status.INTERNAL_SERVER_ERROR.getStatusCode());
         }
    }
    
    public static XMLGregorianCalendar addTimetoDate(XMLGregorianCalendar date) throws HttpException {
        try {
            Date formatedDate = null;
            String time = "T12:00:00";
            String dateTime = date.toString() + time;
            DateFormat df = new SimpleDateFormat(ADD_CLAIM_DATE_FORMAT);
            
            formatedDate = df.parse(dateTime);
            
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(formatedDate);
         
            XMLGregorianCalendar xgc = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
            return xgc;
        } catch (ParseException | DatatypeConfigurationException e) {
            e.printStackTrace();
            throw new HttpException(e.getMessage(),Status.INTERNAL_SERVER_ERROR.getStatusCode());
        }
    }
    
    public static XMLGregorianCalendar setDate(String date) throws HttpException {
        
        try {
            Date formatedDate = null;
            DateFormat df = new SimpleDateFormat(DATE_FORMAT);
            
            if(StringUtils.isNotBlank(date))
                formatedDate = df.parse(date);
            else
                formatedDate = new Date();
            
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(formatedDate);
         
            XMLGregorianCalendar xgc = DatatypeFactory.newInstance().newXMLGregorianCalendarDate(cal.get(Calendar.YEAR),
              cal.get(Calendar.MONTH)+1,cal.get(Calendar.DAY_OF_MONTH),DatatypeConstants.FIELD_UNDEFINED);
            return xgc;
        } catch (ParseException | DatatypeConfigurationException e) {
            e.printStackTrace();
            throw new HttpException(e.getMessage(),Status.INTERNAL_SERVER_ERROR.getStatusCode());
        }
    }
    
    public static XMLGregorianCalendar setDateTime(String date) throws HttpException {
        
        try {
            Date formatedDate = null;
            DateFormat df = new SimpleDateFormat(ADD_CLAIM_DATE_FORMAT);
            
            if(StringUtils.isNotBlank(date))
                formatedDate = df.parse(date);
            else
                formatedDate = new Date();
            
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(formatedDate);
         
            XMLGregorianCalendar xgc = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
            return xgc;
        } catch (ParseException | DatatypeConfigurationException e) {
            e.printStackTrace();
            throw new HttpException(e.getMessage(),Status.INTERNAL_SERVER_ERROR.getStatusCode());
        }
    }
    
    
}
