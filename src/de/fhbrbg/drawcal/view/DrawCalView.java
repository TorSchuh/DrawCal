package de.fhbrbg.drawcal.view;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import de.fhbrbg.drawcal.R;

public class DrawCalView extends View {

	private static final long MILLIS_IN_SECOND = 1000;
	private static final long MILLIS_IN_MINUTE = MILLIS_IN_SECOND * 60;
	private static final long MILLIS_IN_HOUR = MILLIS_IN_MINUTE * 60;
	private static final long MILLIS_IN_DAY = MILLIS_IN_HOUR * 24;
	
    private Context mContext;
    private Paint mViewBackground, mLabelDayBackground, mLabelEventBackground, mLabelTodayBackground, mSeperatorDay, mLabelDay, mLabelDayWeekend, mLabelDayWeekendBackground, mLabelMonth, mLabelMonthBackground;
    
    private Calendar mCalendar;
    private long mTimeNowMillis;
    
    private float mPxMilli, mPxHour, mPxDay;
    
    private int mViewHeight, mViewWidth;
    private long mViewTop, mViewBottom, mViewSpan;
    
    int mFingerCount, mFinger_1_ID, mFinger_2_ID;
    float mFinger_1_Y, mFinger_1_X, mFinger_2_Y, mFinger_2_X;
	    
	public DrawCalView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		init();
	}

	public DrawCalView(Context context, AttributeSet attrs)	{
		super(context, attrs);
		mContext = context;
		init();
	}

	public DrawCalView(Context context)	{
		super(context);
		mContext = context;
		init();
	}
	
	private void init()	{
		
		mCalendar = new GregorianCalendar();
		
		mViewHeight = getHeight();
		mViewWidth = getWidth();

		mCalendar.setTimeInMillis(System.currentTimeMillis());		
		mCalendar.set(Calendar.MILLISECOND, 0);
		mCalendar.set(Calendar.SECOND, 0);
		mCalendar.set(Calendar.MINUTE, 0);
		mCalendar.set(Calendar.HOUR, 0);
		
		mTimeNowMillis = mCalendar.getTimeInMillis();
		
		mViewTop = mTimeNowMillis - MILLIS_IN_DAY;
		mViewBottom = (long) (mTimeNowMillis + MILLIS_IN_DAY * 14);
		mViewSpan = mViewBottom - mViewTop;
		
		mViewBackground = new Paint();
		mViewBackground.setColor(mContext.getResources().getColor(R.color.grey100));
		mViewBackground.setStyle(Paint.Style.FILL);
		
		mLabelEventBackground = new Paint();
		mLabelEventBackground.setColor(mContext.getResources().getColor(R.color.grey400));
		mLabelEventBackground.setStyle(Paint.Style.FILL);
		
		mLabelDayBackground = new Paint();
		mLabelDayBackground.setColor(mContext.getResources().getColor(R.color.grey100));
		mLabelDayBackground.setStyle(Paint.Style.FILL);
		
		mLabelTodayBackground = new Paint();
		mLabelTodayBackground.setColor(mContext.getResources().getColor(R.color.yellow));
		mLabelTodayBackground.setStyle(Paint.Style.FILL);

		mLabelDayWeekendBackground = new Paint();
		mLabelDayWeekendBackground.setColor(mContext.getResources().getColor(R.color.grey150));
		mLabelDayWeekendBackground.setStyle(Paint.Style.FILL);
		
		mLabelMonthBackground = new Paint();
		mLabelMonthBackground.setColor(mContext.getResources().getColor(R.color.rose));
		mLabelMonthBackground.setStyle(Paint.Style.FILL);
		
		mSeperatorDay = new Paint();
		mSeperatorDay.setColor(mContext.getResources().getColor(R.color.grey300));
		mSeperatorDay.setStrokeWidth(1f);
		mSeperatorDay.setAntiAlias(true);
		mSeperatorDay.setStyle(Style.STROKE);
		
		mLabelDay = new Paint();
		mLabelDay.setColor(mContext.getResources().getColor(R.color.grey700));
		mLabelDay.setAntiAlias(true);
		mLabelDay.setTextSize(convertDpToPixel(12, mContext));

		mLabelDayWeekend = new Paint();
		mLabelDayWeekend.setColor(mContext.getResources().getColor(R.color.grey500));
		mLabelDayWeekend.setAntiAlias(true);
		mLabelDayWeekend.setTextSize(convertDpToPixel(12, mContext));
		
		mLabelMonth = new Paint();
		mLabelMonth.setColor(mContext.getResources().getColor(R.color.pink));
		mLabelMonth.setAntiAlias(true);
		mLabelMonth.setTextSize(convertDpToPixel(12, mContext));
		
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
				
		mViewHeight = getHeight();
		mViewWidth = getWidth();
		
		mPxMilli = (float) mViewHeight / mViewSpan;
		mPxHour = (float) MILLIS_IN_HOUR * mPxMilli;
		mPxDay = (float) MILLIS_IN_DAY * mPxMilli;
		
		mCalendar.setTimeInMillis(mViewTop);
		mCalendar.set(Calendar.MILLISECOND, 0);
		mCalendar.set(Calendar.SECOND, 0);
		mCalendar.set(Calendar.MINUTE, 0);
		mCalendar.set(Calendar.HOUR, 0);
		
		canvas.drawPaint(mViewBackground);
		
		long hourNext = mCalendar.getTimeInMillis();
		float offset = 0f;
		int tmpHour = mCalendar.get(Calendar.HOUR_OF_DAY);
		int tmpDay = mCalendar.get(Calendar.DAY_OF_MONTH);
		
		for(long i = hourNext; i < mViewBottom + MILLIS_IN_DAY; i += MILLIS_IN_HOUR) {
			
			offset = ((float) (i - mViewTop) / mViewSpan * (float) mViewHeight);
		
			if (mCalendar.get(Calendar.DAY_OF_WEEK) != tmpDay) {
				
				if ((mCalendar.get(Calendar.DAY_OF_WEEK) % 7) < 2) {
					canvas.drawRect(0, offset-mPxDay+1, mViewWidth, offset, mLabelDayWeekendBackground);	
				} else {
					canvas.drawRect(0, offset-mPxDay+1, mViewWidth, offset, mLabelDayBackground);
				}
				
				canvas.drawLine(convertDpToPixel(50, mContext), offset, mViewWidth, offset, mSeperatorDay);
				
				tmpDay = mCalendar.get(Calendar.DAY_OF_WEEK);
			}
			
			mCalendar.add(Calendar.HOUR_OF_DAY, 1);
			
		}
		
		mCalendar.setTimeInMillis(hourNext);
		
		for(long i = hourNext; i < mViewBottom + MILLIS_IN_DAY; i += MILLIS_IN_HOUR) {
			
			offset = ((float) (i - mViewTop) / mViewSpan * (float) mViewHeight);
					
			if(mCalendar.get(Calendar.HOUR_OF_DAY) != tmpHour) {
				
				if (mPxHour >= mLabelDay.getTextSize()/10) {
					if (mCalendar.get(Calendar.HOUR_OF_DAY) != 0) {
						if (mCalendar.get(Calendar.HOUR_OF_DAY) == 12) {
							canvas.drawLine(convertDpToPixel(60, mContext), offset, convertDpToPixel(65, mContext), offset, mLabelDay);
						}
					}
				}
				if (mPxHour >= mLabelDay.getTextSize()/6) {
					if (mCalendar.get(Calendar.HOUR_OF_DAY) != 0) {
						if (mCalendar.get(Calendar.HOUR_OF_DAY) == 12 || mCalendar.get(Calendar.HOUR_OF_DAY) == 6 || mCalendar.get(Calendar.HOUR_OF_DAY) == 18) {
							canvas.drawLine(convertDpToPixel(60, mContext), offset, convertDpToPixel(75, mContext), offset, mLabelDay);
						} else {
							canvas.drawLine(convertDpToPixel(60, mContext), offset, convertDpToPixel(65, mContext), offset, mLabelDay);
								
						}
					}
				}
				if (mPxHour >= mLabelDay.getTextSize()/3) {
					if (mCalendar.get(Calendar.HOUR_OF_DAY) != 0) {
						if (mCalendar.get(Calendar.HOUR_OF_DAY) == 12 || mCalendar.get(Calendar.HOUR_OF_DAY) == 6 || mCalendar.get(Calendar.HOUR_OF_DAY) == 18) {
							canvas.drawText(mCalendar.get(Calendar.HOUR_OF_DAY)+"", convertDpToPixel(45, mContext), offset+mLabelDay.getTextSize()/2-convertDpToPixel(2, mContext), mLabelDay);
						}
					}
				}
				if (mPxHour >= mLabelDay.getTextSize()) {
					if (mCalendar.get(Calendar.HOUR_OF_DAY) != 0) {
						if (mCalendar.get(Calendar.HOUR_OF_DAY) == 12 || mCalendar.get(Calendar.HOUR_OF_DAY) == 6 || mCalendar.get(Calendar.HOUR_OF_DAY) == 18) {
						} else {
							canvas.drawText(mCalendar.get(Calendar.HOUR_OF_DAY)+"", convertDpToPixel(45, mContext), offset+mLabelDay.getTextSize()/2-convertDpToPixel(2, mContext), mLabelDay);
						}
					}
				}
								
				if (mCalendar.get(Calendar.DAY_OF_MONTH) % 5 == 0) {
					if (tmpHour == 23 && mPxHour < mLabelDay.getTextSize()/10) {
						canvas.drawRect(convertDpToPixel(80, mContext), offset+convertDpToPixel(3, mContext), convertDpToPixel(140, mContext), offset+mPxHour*24f-(convertDpToPixel(2, mContext)), mLabelEventBackground);
						canvas.drawRect(convertDpToPixel(145, mContext), offset+convertDpToPixel(3, mContext), convertDpToPixel(205, mContext), offset+mPxHour*24f-(convertDpToPixel(2, mContext)), mLabelEventBackground);
						canvas.drawRect(convertDpToPixel(210, mContext), offset+convertDpToPixel(3, mContext), convertDpToPixel(270, mContext), offset+mPxHour*24f-(convertDpToPixel(2, mContext)), mLabelEventBackground);
						if (mPxHour*24f >= mLabelDay.getTextSize()) {
							canvas.drawText("Termin X", convertDpToPixel(85, mContext), offset + (mPxHour*24f/2) + (mLabelMonth.getTextSize()/2-convertDpToPixel(2, mContext)) , mLabelDay);
							canvas.drawText("Termin Y", convertDpToPixel(150, mContext), offset + (mPxHour*24f/2) + (mLabelMonth.getTextSize()/2-convertDpToPixel(2, mContext)) , mLabelDay);
							canvas.drawText("Termin Z", convertDpToPixel(215, mContext), offset + (mPxHour*24f/2) + (mLabelMonth.getTextSize()/2-convertDpToPixel(2, mContext)) , mLabelDay);	
						}
					}
					
					if (mPxHour >= mLabelDay.getTextSize()/10) {
						if (tmpHour == 7) {
							canvas.drawRect(convertDpToPixel(80, mContext), offset, convertDpToPixel(140, mContext), offset+mPxHour*3f, mLabelEventBackground);
							canvas.drawText("Termin X", convertDpToPixel(85, mContext), offset + (mPxHour*3f/2) + (mLabelMonth.getTextSize()/2-convertDpToPixel(2, mContext)) , mLabelDay);
						}
						
						if (tmpHour == 12) {
							canvas.drawRect(convertDpToPixel(145, mContext), offset, convertDpToPixel(205, mContext), offset+mPxHour*4.5f, mLabelEventBackground);
							canvas.drawText("Termin Y", convertDpToPixel(150, mContext), offset + (mPxHour*4.5f/2) + (mLabelMonth.getTextSize()/2-convertDpToPixel(2, mContext)) , mLabelDay);
						}
						
						if (tmpHour == 19) {
							canvas.drawRect(convertDpToPixel(210, mContext), offset, convertDpToPixel(270, mContext), offset+mPxHour*1.5f, mLabelEventBackground);
							canvas.drawText("Termin Z", convertDpToPixel(215, mContext), offset + (mPxHour*1.5f/2) + (mLabelMonth.getTextSize()/2-convertDpToPixel(2, mContext)) , mLabelDay);
						}
					}
				}
								
				if (mCalendar.get(Calendar.DAY_OF_MONTH) % 6 == 0) {
					if (tmpHour == 23 && mPxHour < mLabelDay.getTextSize()/10) {
						canvas.drawRect(convertDpToPixel(80, mContext), offset+convertDpToPixel(3, mContext), convertDpToPixel(140, mContext), offset+mPxHour*24f-(convertDpToPixel(2, mContext)), mLabelEventBackground);
						canvas.drawRect(convertDpToPixel(145, mContext), offset+convertDpToPixel(3, mContext), convertDpToPixel(205, mContext), offset+mPxHour*24f-(convertDpToPixel(2, mContext)), mLabelEventBackground);
						canvas.drawRect(convertDpToPixel(210, mContext), offset+convertDpToPixel(3, mContext), convertDpToPixel(270, mContext), offset+mPxHour*24f-(convertDpToPixel(2, mContext)), mLabelEventBackground);
						if (mPxHour*24f >= mLabelDay.getTextSize()) {
							canvas.drawText("Termin A", convertDpToPixel(85, mContext), offset + (mPxHour*24f/2) + (mLabelMonth.getTextSize()/2-convertDpToPixel(2, mContext)) , mLabelDay);
							canvas.drawText("Termin B", convertDpToPixel(150, mContext), offset + (mPxHour*24f/2) + (mLabelMonth.getTextSize()/2-convertDpToPixel(2, mContext)) , mLabelDay);
							canvas.drawText("Termin C", convertDpToPixel(215, mContext), offset + (mPxHour*24f/2) + (mLabelMonth.getTextSize()/2-convertDpToPixel(2, mContext)) , mLabelDay);	
						}
					}
					
					if (mPxHour >= mLabelDay.getTextSize()/10) {
						if (tmpHour == 7) {
							canvas.drawRect(convertDpToPixel(80, mContext), offset, convertDpToPixel(140, mContext), offset+mPxHour*3f, mLabelEventBackground);
							canvas.drawText("Termin A", convertDpToPixel(85, mContext), offset + (mPxHour*3f/2) + (mLabelMonth.getTextSize()/2-convertDpToPixel(2, mContext)) , mLabelDay);
						}
						
						if (tmpHour == 12) {
							canvas.drawRect(convertDpToPixel(80, mContext), offset, convertDpToPixel(140, mContext), offset+mPxHour*4.5f, mLabelEventBackground);
							canvas.drawText("Termin B", convertDpToPixel(85, mContext), offset + (mPxHour*4.5f/2) + (mLabelMonth.getTextSize()/2-convertDpToPixel(2, mContext)) , mLabelDay);
						}
						
						if (tmpHour == 19) {
							canvas.drawRect(convertDpToPixel(80, mContext), offset, convertDpToPixel(140, mContext), offset+mPxHour*1.5f, mLabelEventBackground);
							canvas.drawText("Termin C", convertDpToPixel(85, mContext), offset + (mPxHour*1.5f/2) + (mLabelMonth.getTextSize()/2-convertDpToPixel(2, mContext)) , mLabelDay);
						}
					}
				}
				
				tmpHour = mCalendar.get(Calendar.HOUR_OF_DAY);
			}
			
			mCalendar.add(Calendar.HOUR_OF_DAY, 1);
		}
		
		mCalendar.setTimeInMillis(hourNext);
		
		for(long i = hourNext; i < mViewBottom + MILLIS_IN_DAY; i += MILLIS_IN_HOUR) {
			
			offset = ((float) (i - mViewTop) / mViewSpan * (float) mViewHeight);
		
			if (mCalendar.get(Calendar.DAY_OF_WEEK) != tmpDay) {

				if (mCalendar.get(Calendar.DAY_OF_MONTH) == 1) {
					canvas.drawRect(0, offset-mPxDay+1, mViewWidth, offset, mLabelMonthBackground);
				}
		
				if (isToday(mCalendar)) {
					canvas.drawRect(0, offset-mPxDay+1, mViewWidth, offset, mLabelTodayBackground);
				}
				
				float offsetLabel = offset-mPxDay/2+mLabelDay.getTextSize()/2-1;

				if (mCalendar.get(Calendar.DAY_OF_MONTH) == 1) {
					switch (mCalendar.get(Calendar.MONTH) % 12) {
						case 0:
							canvas.drawText("Januar", convertDpToPixel(55, mContext), offsetLabel, mLabelMonth);
							break;
						case 1:
							canvas.drawText("Februar", convertDpToPixel(55, mContext), offsetLabel, mLabelMonth);
							break;
						case 2:
							canvas.drawText("März", convertDpToPixel(55, mContext), offsetLabel, mLabelMonth);
							break;
						case 3:
							canvas.drawText("April", convertDpToPixel(55, mContext), offsetLabel, mLabelMonth);
							break;
						case 4:
							canvas.drawText("Mai", convertDpToPixel(55, mContext), offsetLabel, mLabelMonth);
							break;
						case 5:
							canvas.drawText("Juni", convertDpToPixel(55, mContext), offsetLabel, mLabelMonth);
							break;
						case 6:
							canvas.drawText("Juli", convertDpToPixel(55, mContext), offsetLabel, mLabelMonth);
							break;
						case 7:
							canvas.drawText("August", convertDpToPixel(55, mContext), offsetLabel, mLabelMonth);
							break;
						case 8:
							canvas.drawText("September", convertDpToPixel(55, mContext), offsetLabel, mLabelMonth);
							break;
						case 9:
							canvas.drawText("Oktober", convertDpToPixel(55, mContext), offsetLabel, mLabelMonth);
							break;
						case 10:
							canvas.drawText("November", convertDpToPixel(55, mContext), offsetLabel, mLabelMonth);
							break;
						case 11:
							canvas.drawText("Dezember", convertDpToPixel(55, mContext), offsetLabel, mLabelMonth);
							break;
					}	
				}
				
				if (mPxDay >= mLabelDay.getTextSize()) {					
					canvas.drawText(String.valueOf(mCalendar.get(Calendar.DAY_OF_MONTH)), convertDpToPixel(25, mContext), offsetLabel, mLabelDay);
					switch (mCalendar.get(Calendar.DAY_OF_WEEK) % 7) {
						case 0:
							canvas.drawText("S", convertDpToPixel(10, mContext), offsetLabel, mLabelDayWeekend);
							break;
						case 1:
							canvas.drawText("S", convertDpToPixel(10, mContext), offsetLabel, mLabelDayWeekend);
							break;
						case 2:
							canvas.drawText("M", convertDpToPixel(10, mContext), offsetLabel, mLabelDay);
							break;
						case 3:
							canvas.drawText("D", convertDpToPixel(10, mContext), offsetLabel, mLabelDay);
							break;
						case 4:
							canvas.drawText("M", convertDpToPixel(10, mContext), offsetLabel, mLabelDay);
							break;
						case 5:
							canvas.drawText("D", convertDpToPixel(10, mContext), offsetLabel, mLabelDay);
							break;
						case 6:
							canvas.drawText("F", convertDpToPixel(10, mContext), offsetLabel, mLabelDay);
							break;
					}
				} else {
					if (mCalendar.get(Calendar.DAY_OF_WEEK) % 7 == 2) { 
						canvas.drawText(String.valueOf(mCalendar.get(Calendar.DAY_OF_MONTH)), convertDpToPixel(25, mContext), offsetLabel, mLabelDay);
						canvas.drawText("M", convertDpToPixel(10, mContext), offsetLabel, mLabelDay);						
					}
				}
				
				tmpDay = mCalendar.get(Calendar.DAY_OF_WEEK);
			}
			
			mCalendar.add(Calendar.HOUR_OF_DAY, 1);			
		}		
	}
	
	
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		switch (event.getActionMasked()) {
		
		
			case MotionEvent.ACTION_DOWN:
				
				mFingerCount = 1;
				
				mFinger_1_ID = event.getPointerId(event.getActionIndex());
				mFinger_1_Y = event.getY();
				mFinger_1_X = event.getX();
				
				invalidate();
				return true;

			case MotionEvent.ACTION_POINTER_DOWN:

				if (mFingerCount == 2) {
					break;
				} else {
					mFingerCount = 2;	
				}
				
				mFinger_2_ID = event.getPointerId(event.getActionIndex());
				mFinger_2_Y = event.getY(mFinger_2_ID);
				mFinger_2_X = event.getX(mFinger_2_ID);

				invalidate();
				return true;

			case MotionEvent.ACTION_MOVE:
								
				if (mFingerCount == 0) {
					return false;
				}
				
				float mFinger_1_X_TMP, mFinger_1_Y_TMP, mFinger_2_X_TMP, mFinger_2_Y_TMP;

				int mPointerIndex = event.findPointerIndex(mFinger_1_ID);
				if (mPointerIndex == -1) {
					mFinger_1_Y_TMP = mFinger_1_Y;
					mFinger_1_X_TMP = mFinger_1_X;
				} else {
					mFinger_1_Y_TMP = event.getY(mPointerIndex);
					mFinger_1_X_TMP = event.getX(mPointerIndex);
				}

				mPointerIndex = event.findPointerIndex(mFinger_2_ID);
				if (mPointerIndex == -1) {
					mFinger_2_Y_TMP = mFinger_2_Y;
					mFinger_2_X_TMP = mFinger_2_X;
				} else {
					mFinger_2_Y_TMP = event.getY(mPointerIndex);
					mFinger_2_X_TMP = event.getX(mPointerIndex);
				}

				if (mFingerCount == 1) {
					long mDelta_1_Y_Millis = (long) ((mFinger_1_Y - mFinger_1_Y_TMP) * mViewSpan / mViewHeight);
					mViewTop += mDelta_1_Y_Millis;
					mViewBottom += mDelta_1_Y_Millis;
				} else if (mFingerCount == 2) {

					if (Math.abs(mFinger_1_Y_TMP - mFinger_2_Y_TMP) < 10) {
						return true;
					}
					if (mFinger_1_Y > mFinger_2_Y) {
						if (mFinger_1_Y_TMP < mFinger_2_Y_TMP) {
							return true;
						}
					}
					if (mFinger_1_Y < mFinger_2_Y) {
						if (mFinger_1_Y_TMP > mFinger_2_Y_TMP) {
							return true;
						}
					}
					
					double mMillisMultiplicator = (double) mViewSpan / (double) mViewHeight;
					double mFinger_1_Y_NEW = mMillisMultiplicator * mFinger_1_Y + mViewTop;
					double mFinger_2_Y_NEW = mMillisMultiplicator * mFinger_2_Y + mViewTop;
					mViewTop = (long) (mFinger_1_Y_NEW + (0 - mFinger_1_Y_TMP) * (mFinger_2_Y_NEW - mFinger_1_Y_NEW) / (mFinger_2_Y_TMP - mFinger_1_Y_TMP));
					mViewBottom = (long) (mFinger_1_Y_NEW + (mViewHeight - mFinger_1_Y_TMP) * (mFinger_2_Y_NEW - mFinger_1_Y_NEW) / (mFinger_2_Y_TMP - mFinger_1_Y_TMP));
					mViewSpan = mViewBottom - mViewTop;
				}

				mFinger_1_Y = mFinger_1_Y_TMP;
				mFinger_1_X = mFinger_1_X_TMP;
				mFinger_2_Y = mFinger_2_Y_TMP;
				mFinger_2_X = mFinger_2_X_TMP;

				invalidate();
				return true;

			case MotionEvent.ACTION_POINTER_UP:
				
				int id = event.getPointerId(event.getActionIndex());

				if (id == mFinger_1_ID)	{

					mFinger_1_ID = mFinger_2_ID;
					mFinger_1_Y = mFinger_2_Y;
					mFinger_1_X = mFinger_2_X;
					mFingerCount = 1;

				} else if (id == mFinger_2_ID) {
					mFingerCount = 1; 
				} else {
					return false;
				}
				
				invalidate();
				return true;

			case MotionEvent.ACTION_CANCEL:
				return true;

			case MotionEvent.ACTION_UP:
				mFingerCount = 0;
				invalidate();
				return true;
			}
		return super.onTouchEvent(event);
	}

	
	private float convertDpToPixel(float dp, Context context){
	    Resources resources = context.getResources();
	    DisplayMetrics metrics = resources.getDisplayMetrics();
	    float px = dp * (metrics.densityDpi / 160f);
	    return px;
	}
	
	private boolean isToday(Calendar calendar) {
		return calendar.get(Calendar.ERA) == Calendar.getInstance().get(Calendar.ERA) && calendar.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
	}
	
}

