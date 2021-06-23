package au.csiro.doi.svc.dto;

import java.io.Serializable;

/**
 * Class representing a Creator.
 * 
 * Can be an organisation or person.
 * 
 * Copyright 2012, CSIRO Australia All rights reserved.
 * 
 * @author Chris Trapani on 13/11/2018
 * @version $Revision$ $Date$
 */
public class GeoLocation implements Serializable
{
    /** Generated serial version **/
    private static final long serialVersionUID = 4236374370542494432L;

    /**
     * Geo Location type
     */
    public enum LocationType
    {
        /**
         * Point location consisting of lat / lon.
         */
        POINT,

        /**
         * Area. Consisiting of 4 coords (WGS84) that specify a rectangular area.
         */
        AREA;
    }

    private LocationType locationType;

    private String north, south, east, west;
    private String latitude, longitude;

    /**
     * Create a geo location for an area.
     * 
     * @param n
     *            North
     * @param s
     *            South
     * @param e
     *            East
     * @param w
     *            West
     */
    public GeoLocation(String n, String s, String e, String w)
    {
        this.north = n;
        this.east = e;
        this.south = s;
        this.west = w;
        this.locationType = LocationType.AREA;
    }

    /**
     * Create a geo location for a point.
     * 
     * @param latitude
     *            The latitude
     * @param longitude
     *            The longitude
     */
    public GeoLocation(String latitude, String longitude)
    {
        this.latitude = latitude;
        this.longitude = longitude;
        this.locationType = LocationType.POINT;
    }

    public LocationType getLocationType()
    {
        return locationType;
    }

    /**
     * Check if this geo location instance is an Area.
     * 
     * @return True if an area, otherwise, false.
     */
    public boolean isArea()
    {
        return locationType == LocationType.AREA;
    }

    /**
     * Check if this geo location instance is a Point.
     * 
     * @return True if a point, otherwise, false.
     */
    public boolean isPoint()
    {
        return locationType == LocationType.POINT;
    }

    public void setLocationType(LocationType locationType)
    {
        this.locationType = locationType;
    }

    public String getNorth()
    {
        return north;
    }

    public void setNorth(String north)
    {
        this.north = north;
    }

    public String getSouth()
    {
        return south;
    }

    public void setSouth(String south)
    {
        this.south = south;
    }

    public String getEast()
    {
        return east;
    }

    public void setEast(String east)
    {
        this.east = east;
    }

    public String getWest()
    {
        return west;
    }

    public void setWest(String west)
    {
        this.west = west;
    }

    public String getLatitude()
    {
        return latitude;
    }

    public void setLatitude(String latitude)
    {
        this.latitude = latitude;
    }

    public String getLongitude()
    {
        return longitude;
    }

    public void setLongitude(String longitude)
    {
        this.longitude = longitude;
    }

}
