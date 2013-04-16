
package org.mule.module.chatter.config;

import org.mule.api.Capabilities;
import org.mule.api.Capability;
import org.mule.module.chatter.ChatterModule;


/**
 * A <code>ChatterModuleCapabilitiesAdapter</code> is a wrapper around {@link ChatterModule } that implements {@link org.mule.api.Capabilities} interface.
 * 
 */
public class ChatterModuleCapabilitiesAdapter
    extends ChatterModule
    implements Capabilities
{


    /**
     * Returns true if this module implements such capability
     * 
     */
    public boolean isCapableOf(Capability capability) {
        if (capability == Capability.LIFECYCLE_CAPABLE) {
            return true;
        }
        return false;
    }

}
