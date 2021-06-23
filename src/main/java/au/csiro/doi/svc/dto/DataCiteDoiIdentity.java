/**
 * Copyright 2012, CSIRO Australia.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package au.csiro.doi.svc.dto;




/**
 * Represents the identify information required when calling the ANDS Digital Object Identifier service.
 * 
 * Copyright 2012, CSIRO Australia All rights reserved.
 * 
 * @author John Page on 03/03/2012
 * 
 * @version $Revision$ $Date$
 */
public class DataCiteDoiIdentity
{
    /**
     * The unique Id provided to the caller upon IP registration with the ANDS Digital Object Identifier service.
     */
    private String appId;

    /**
     * The domain of the organisation calling the service.
     */
    private String authDomain;




    /**
     * Default constructor
     */
    public DataCiteDoiIdentity()
    {
    }

    /**
     * Constructor
     * 
     * @param appId
     *            the unique Id provided to the caller upon IP registration with the ANDS Digital Object Identifier
     *            service.
     * @param authDomain
     *            the domain of the organisation calling the service.
     */
    @SuppressWarnings(value = "all")
    public DataCiteDoiIdentity(String appId, String authDomain)
    {
        this.setAppId(appId);
        this.setAuthDomain(authDomain);
    }

    /**
     * @return the appId
     */
    public String getAppId()
    {
        return escape(appId);
    }

    /**
     * @param appId
     *            the appId to set
     */
    public void setAppId(String appId)
    {
        this.appId = appId;
    }


    /**
     * @return the authDomain
     */
    public String getAuthDomain()
    {
        return escape(authDomain);
    }

    /**
     * @param authDomain
     *            the authDomain to set
     */
    public void setAuthDomain(String authDomain)
    {
        this.authDomain = authDomain;
    }




    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "RequestorIdentity [appId=" + appId + ", authDomain=" + authDomain + "]";
    }

    /**
     * Escape reserved characters, assumes UTF-8 or UTF-16 as encoding.
     * 
     * @param in
     *            the String whose reserved characters we want to remove.
     * @return the in String, stripped of reserved characters.
     */
    public String escape(String in)
    {
        if (in == null)
        {
            return in;
        }

        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < in.length(); i++)
        {
            char c = in.charAt(i);
            if (c == '<')
            {
                buffer.append("&lt;");
            }
            else if (c == '>')
            {
                buffer.append("&gt;");
            }
            else if (c == '&')
            {
                buffer.append("&amp;");
            }
            else if (c == '"')
            {
                buffer.append("&quot;");
            }
            else if (c == '\'')
            {
                buffer.append("&apos;");
            }
            else
            {
                buffer.append(c);
            }
        }
        return buffer.toString();
    }


}

