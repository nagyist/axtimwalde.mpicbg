/*-
 * #%L
 * MPICBG Core Library.
 * %%
 * Copyright (C) 2008 - 2025 Stephan Saalfeld et. al.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 2 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-2.0.html>.
 * #L%
 */
/**
 * License: GPL
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License 2
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package mpicbg.ij.integral;

import ij.process.ByteProcessor;
import ij.process.ColorProcessor;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;
import ij.process.ShortProcessor;

/**
 * 
 *
 * @author Stephan Saalfeld &lt;saalfeld@mpi-cbg.de&gt;
 * @version 0.1a
 */
public class Tilt
{
	final private IntegralImage integral;
	final private ImageProcessor ip;
	
	public Tilt( final ColorProcessor ip )
	{
		this.ip = ip;
		integral = new LongRGBIntegralImage( ip );
	}
	
	public Tilt( final ByteProcessor ip )
	{
		this.ip = ip;
		integral = new LongIntegralImage( ip );
	}
	
	public Tilt( final ShortProcessor ip )
	{
		this.ip = ip;
		integral = new LongIntegralImage( ip );
	}

	public Tilt( final FloatProcessor ip )
	{
		this.ip = ip;
		integral = new DoubleIntegralImage( ip );
	}
	
	/**
	 * Factory method that decides on the type of <code>ip</code> which
	 * {@link Tilt} constructor to call.  Returns <code>null</code> for unknown
	 * types.
	 * 
	 * @param ip
	 * @return
	 */
	static public Tilt create( final ImageProcessor ip )
	{
		if (ip instanceof FloatProcessor)
			return new Tilt( ( FloatProcessor )ip );
		else if (ip instanceof ByteProcessor)
			return new Tilt( ( ByteProcessor )ip );
		else if (ip instanceof ShortProcessor)
			return new Tilt( ( ShortProcessor )ip );
		else if (ip instanceof ColorProcessor)
			return new Tilt( ( ColorProcessor )ip );
		else
			return null;
	}

	public void render( final int x1, final int y1, final int x2, final int y2 )
	{
		final int w = ip.getWidth() - 1;
		final int h = ip.getHeight() - 1;
		final double s = ( ip.getWidth() + ip.getHeight() ) * 2.0;
		
		final int dx = x2 - x1;
		final int dy = y2 - y1;
		
		for ( int y = 0; y <= h; ++y )
		{
			final double yt = y - y1;
			for ( int x = 0; x <= w; ++x )
			{
				final double xt = x - x1;
				
				final double r = ( dx * xt + dy * yt ) / s;
				final int ri = r < 0 ? ( int )-r : ( int )r;
				
				final int yMin = Math.max( -1, y - ri - 1 );
				final int yMax = Math.min( h, y + ri );
				final int bh = yMax - yMin;
				final int xMin = Math.max( -1, x - ri - 1 );
				final int xMax = Math.min( w, x + ri );
				final float scale = 1.0f / ( xMax - xMin ) / bh;
				ip.set( x, y, integral.getScaledSum( xMin, yMin, xMax, yMax, scale ) );
			}
		}
	}
}
