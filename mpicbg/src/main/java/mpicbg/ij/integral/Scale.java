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
 */
final public class Scale
{
	final private IntegralImage integral;
	final private ImageProcessor ip;
	
	public Scale( final ColorProcessor ip )
	{
		this.ip = ip;
		integral = new LongRGBIntegralImage( ip );
	}
	
	public Scale( final ByteProcessor ip )
	{
		this.ip = ip;
		integral = new LongIntegralImage( ip );
	}
	
	public Scale( final ShortProcessor ip )
	{
		this.ip = ip;
		integral = new LongIntegralImage( ip );
	}

	public Scale( final FloatProcessor ip )
	{
		this.ip = ip;
		integral = new DoubleIntegralImage( ip );
	}
	
	static private int round( final double a )
	{
		return ( int )( a + Math.signum( a ) * 0.5f );
	}
	
	public ImageProcessor scale( final int width, final int height )
	{
		final int w = width - 1;
		final int h = height - 1;
		
		final int ww = ip.getWidth() - 1;
		final int hh = ip.getHeight() - 1;
		
		final ImageProcessor target = ip.createProcessor( width, height );
		final double pixelWidth = ( double )ip.getWidth() / width;
		final double pixelHeight = ( double )ip.getHeight() / height;
		
		for ( int y = 0; y < height; ++y )
		{
			final int yi = width * Math.min(h, y);
			final double yMinDouble = y * pixelHeight;
			final int yMin = Math.min( hh, Math.max( -1, round( yMinDouble ) - 1 ) );
			final int yMax = Math.max( -1, Math.min( hh, round( yMinDouble + pixelHeight - 1 ) ) );
			final int bh = yMax - yMin;
			for ( int x = 0; x < width; ++x )
			{
				final int xi = Math.min(w, x);
				final double xMinDouble = x * pixelWidth;
				final int xMin = Math.min( ww, Math.max( -1, round( xMinDouble ) - 1 ) );
				final int xMax = Math.min( ww, Math.max( -1, round( xMinDouble + pixelWidth - 1 ) ) );
				final float scale = 1.0f / ( xMax - xMin ) / bh;
				target.set( yi + xi, integral.getScaledSum( xMin, yMin, xMax, yMax, scale ) );
			}
		}
		
		return target;
	}
	
	public ImageProcessor scale( final double scale )
	{
		final int width = round( ip.getWidth() * scale );
		final int height = round( ip.getHeight() * scale );
		
		return scale( width, height );
	}
	
	
	/**
	 * Factory method that decides on the type of <code>ip</code> which
	 * {@link Scale} constructor to call.  Returns <code>null</code> for
	 * unknown types.
	 * 
	 * @param ip
	 * @return
	 */
	static public Scale create( final ImageProcessor ip )
	{
		if (ip instanceof FloatProcessor)
			return new Scale( ( FloatProcessor )ip );
		else if (ip instanceof ByteProcessor)
			return new Scale( ( ByteProcessor )ip );
		else if (ip instanceof ShortProcessor)
			return new Scale( ( ShortProcessor )ip );
		else if (ip instanceof ColorProcessor)
			return new Scale( ( ColorProcessor )ip );
		else
			return null;
	}
}
