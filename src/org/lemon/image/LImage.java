package org.lemon.image;

import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;

import org.lemon.filter.GrayImageFilter;
import org.lemon.graphics.ImageGraphics;

/**
 * 
 * Image data is represented in 1 dimensional int array 
 * on {@code LImage}. But can be accessed with 2 dimensional coordinate.
 * 
 * */
public class LImage extends ImageGraphics {
	
	public final static int GRAY = 0xA;
	public final static int DEFAULT = 0xB;

	private BufferedImage asBufferedImg = null;
	private Raster raster = null;
	private DataBuffer data = null;
	private int[] dataArr = null;
	
	private boolean disposed = false;
	
	public int width = 0;
	public int height = 0;
	public int typ = DEFAULT;
	
	public LImage( int w, int h, int type ) {
		this( null, w, h, type );
	}
	
	
	public LImage( int w, int h, boolean alpha, int type ) {
		this( null, w, h, type );
	}
	
	
	public LImage( BufferedImage img ) {
		asBufferedImg = img;
		width = img.getWidth();
		height = img.getHeight();
		raster = img.getData();
		data = raster.getDataBuffer();
		dataArr = new int[width * height];
	}
	
	
	public LImage( Raster raster, int w, int h, int type ) {
		width = w;
		height = h;
		typ = type;
		
		asBufferedImg = new BufferedImage( w, h, BufferedImage.TYPE_INT_RGB );
		
		if( raster != null ) {
			
			asBufferedImg.setData( raster );
			this.raster = raster;
			data = raster.getDataBuffer();
			
			int dataType = data.getDataType();
			
			switch( dataType ) {
			
			case DataBuffer.TYPE_BYTE: {
				break;
			}
			
			case DataBuffer.TYPE_INT: {
				dataArr = (int[]) raster.getDataElements( 0, 0, w, h, dataArr );
				break;
			}
			
			}
			
		}
		else {
			dataArr = new int[w * h];
		}
		
		initImageType( type );
	}
	
	
	private void initImageType( int type ) {
		switch( type ) {
		
		case GRAY: {
			GrayImageFilter gray = new GrayImageFilter();
			gray.filter( this );
		}
		break;
		
		default: {
			
		}
		break;
		
		}
	}
	
	
	@Override
	public void draw( Shape shape ) {
		
		if( disposed ) 
			return;
		
	}
	
	
	@Override
	public void dispose() {
		disposed = true;
	}
	
	
	@Override
	public void clear() {
		
	}
	
	/***
	 * Blend this image with specified image.
	 * 
	 * @param img 		Image to blend with.
	 * @param c 		Blend composite.
	 */
	public void composite( LImage img, Composite c ) {
		
		Graphics2D g2 = getAsBufferedImage().createGraphics();
		g2.setComposite( c );
		g2.drawImage( img.getAsBufferedImage(), 0, 0, null );
		g2.dispose();
	}
	
	
	/**
	* 
	* Set the pixels to this image.
	* 
	* @param x       the left edge of the pixel block
    * @param y       the right edge of the pixel block
    * @param width   the width of the pixel arry
    * @param height  the height of the pixel arry
    * @param pixels  the array of pixels to set
	*
	*/
	public void setPixels( int x, int y, int width, int height, int[] pixels ) {
		BufferedImage image = getAsBufferedImage();
		int type = image.getType();
		
		if ( type == BufferedImage.TYPE_INT_ARGB || type == BufferedImage.TYPE_INT_RGB )
			image.getRaster().setDataElements( x, y, width, height, pixels );
		else
			image.setRGB( x, y, width, height, pixels, 0, width );	
	}
	
	
	/**
	 * Get the all pixels of this image.
	 * 
     * @param x       the left edge of the pixel block
     * @param y       the right edge of the pixel block
     * @param width   the width of the pixel arry
     * @param height  the height of the pixel arry
     * @param pixels  the array to hold the returned pixels. May be null.
     * @return the pixels
     */
	public int[] getPixels( int x, int y, int width, int height, int[] pixels ) {
		BufferedImage image = getAsBufferedImage();
		int type = image.getType();
		
		if ( type == BufferedImage.TYPE_INT_ARGB || type == BufferedImage.TYPE_INT_RGB )
			return (int [])image.getRaster().getDataElements( x, y, width, height, pixels );
		return image.getRGB( x, y, width, height, pixels, 0, width );
    }
	
	
	/**
	 * Change specified x, y coordinated pixel on image.
	 * 
	 * @param x 		x-coord
	 * @param y 		y-coord
	 * @param pixData	pixel data 
	 * */
	public void setPixel( int x, int y, int pixData ) {
		dataArr[x * width + y] = pixData;
	}
	
	/**
	 * Get specific x, y coordinated pixel from image.
	 * 
	 * @param x 		x-coord
	 * @param y 		y-coord
	 * @return Pixel data of that coordinate
	 * */
	public int getPixel( int x, int y ) {
		return dataArr[x * width + y];
	}
	
	/**
	 * Get {@code DataBuffer} attached with this image.
	 * 
	 * @return {@code DataBuffer} 
	 * */
	public DataBuffer getDataBuffer() {
		return data;
	}
	
	/**
	 * Get {@code Raster} attached with this image.
	 * 
	 * @return {@code Raster} data 
	 * */
	public Raster getRaster() {
		return raster;
	}
	
	/** 
	 * Get {@code this LImage} as {@code BufferedImage}.
	 * 
	 * @return {@code BufferedImage} version of this {@code LImage}
	 * */
	public BufferedImage getAsBufferedImage() {
		return asBufferedImg;
	}
}
