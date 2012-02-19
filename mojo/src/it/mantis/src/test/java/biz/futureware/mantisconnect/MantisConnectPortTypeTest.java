package biz.futureware.mantisconnect;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class MantisConnectPortTypeTest
{
    private MantisConnectPortType service;

    @Before
    public void setUp()
        throws Exception
    {
        service = new MantisConnectPortTypeImpl();
    }

    @Test
    public void getVersion()
        throws Exception
    {
        String version = service.mc_version();
        assertThat( version, is( notNullValue() ) );
        assertThat( version.isEmpty(), is( false ) );
        System.out.printf( "mc_version = %s%n", version );
    }

}
