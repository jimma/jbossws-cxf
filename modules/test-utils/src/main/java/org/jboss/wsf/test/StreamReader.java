package org.jboss.wsf.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.jboss.logging.Logger;

public final class StreamReader implements Runnable {
        private static Logger log = Logger.getLogger(StreamReader.class);

        /**
         * Input stream to data read from.
         */
        private final InputStream is;
        /**
         * Output stream to write data to.
         */
        private final OutputStream os;
        /**
         * Whether this job is terminated.
         */
        private volatile boolean terminated;

        private final boolean closeOsOnExit;

        /**
         * Constructor; the input stream will automatically be closed on exit, while the output stream won't.
         *
         * @param is input stream to read data from
         * @param os output stream to write data to
         */
   public StreamReader( InputStream is, OutputStream os )
        {
            this(is, os, false);
        }

        /**
         *
         * Constructor.
         * @param is input stream to read data from
         * @param os output stream to write data to
         * @param closeOutputStreamOnExit whether to flush and close the output stream on exit
         */
   public StreamReader( InputStream is, OutputStream os, boolean closeOutputStreamOnExit) {
        super();

        if ( ( is == null ) || ( os == null ) )
        {
            throw new IllegalArgumentException( "Constructor parameters can't be null" );
        }

        this.is = is;
        this.os = os;
        this.closeOsOnExit = closeOutputStreamOnExit;
    }

        /**
         * Copies all data from <b>input stream</b> to <b>output stream</b> (both passed to constructor) until job is killed
         */
        public final void run()
        {
            try
            {
                copy( this.is, this.os );
            }
            catch ( IOException ioe )
            {
                log.error(ioe);
            }
            finally
            {
                try
                {
                    this.is.close();
                }
                catch (IOException ioe)
                {
                    log.error(ioe);
                }
                if (this.closeOsOnExit)
                {
                    try
                    {
                        this.os.flush();
                        this.os.close();
                    }
                    catch (IOException ioe)
                    {
                        log.error(ioe);
                    }
                }
            }
        }

        /**
         * Copies all data from <b>is</b> to <b>os</b> until job is killed
         * @param is input stream to read data from
         * @param os output stream to write data to
         * @throws IOException if I/O error occurs
         */
        private void copy( final InputStream is, final OutputStream os ) throws IOException
        {
            final byte[] buffer = new byte[ 512 ];
            int countOfBytes = -1;

            while ( !this.terminated )
            {
                while ( is.available() <= 0 )
                {
                    synchronized( this )
                    {
                        try
                        {
                            this.wait( 50 ); // guard
                            if ( this.terminated ) return;
                        }
                        catch ( InterruptedException ie )
                        {
                            log.error(ie);
                        }
                    }
                }

                countOfBytes = is.read( buffer, 0, buffer.length );
                os.write( buffer, 0, countOfBytes );
            }
        }

        /**
         * Kills this job. Calling this method also ensures that input stream passed to the constructor will be closed properly
         */
        public final void kill()
        {
            this.terminated = true;
        }

    }