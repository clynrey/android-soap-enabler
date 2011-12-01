/**
 * Android SOAP Enabler is free software: you can redistribute it and/or modify
 * it under the terms of the Lesser GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, 
 * or any later version.
 * 
 * Android SOAP Enabler is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the Lesser GNU General Public License
 * along with Android SOAP Enabler.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * ©2011, Android SOAP Enabler Development Team
 */
package fr.norsys.asoape.log;

/**
 * Logger interface. Use <code>.is&lt;LEVEL>Enabled()</code> method before build a complex message to save memory and CPU.
 * 
 * @since 1.0
 */
public interface Logger
{
    /**
     * @return Return <code>true</code> if this logger will log messages in debug mode, <code>false</code> otherwise.
     */
    boolean isDebugEnabled();

    /**
     * Display the message in debug mode.
     * 
     * @param message Message to display.
     */
    void debug( String message );

    /**
     * Display the message and the {@link Throwable} stack trace in debug mode.
     * 
     * @param message Message to display.
     * @param throwable Caught throwable.
     */
    void debug( String message, Throwable throwable );

    /**
     * @return Return <code>true</code> if this logger will log messages in error mode, <code>false</code> otherwise.
     */
    boolean isErrorEnabled();

    /**
     * Display the message in error mode.
     * 
     * @param message Message to display.
     */
    void error( String message );

    /**
     * Display the message and the {@link Throwable} stack trace in error mode.
     * 
     * @param message Message to display.
     * @param throwable Caught throwable.
     */
    void error( String message, Throwable throwable );
}
