package cz.cvut.kbss.ear.project.kosapi.entities;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import cz.cvut.kbss.ear.project.kosapi.links.RoomLink;

import java.io.Serializable;

public class KosTimetableSlot implements Serializable {
    @JacksonXmlProperty(localName = "day")
    private String day;

    @JacksonXmlProperty(localName = "duration")
    private String duration;

    @JacksonXmlProperty(localName = "endTime")
    private String endTime;

    @JacksonXmlProperty(localName = "parity")
    private String parity;

    @JacksonXmlProperty(localName = "firstHour")
    private String firstHour;

    @JacksonXmlProperty(localName = "startTime")
    private String startTime;

    @JacksonXmlProperty(localName = "room")
    private RoomLink room;
}
