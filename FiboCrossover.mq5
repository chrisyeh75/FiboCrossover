//+------------------------------------------------------------------+
//|                                                FiboCrossover.mq5 |
//|                        Copyright 2017, MetaQuotes Software Corp. |
//|                                             https://www.mql5.com |
//+------------------------------------------------------------------+
#property copyright "Copyright 2017, MetaQuotes Software Corp."
#property link      "https://www.mql5.com"
#property version   "1.00"
//+------------------------------------------------------------------+
//| Expert initialization function                                   |
//+------------------------------------------------------------------+


int period = 97;
int ema_period = 19;
int fsma_period = 17;
int ssma_period = 18;
double abs = 0.0001;
double tp_level = 165.8;
double tp_level2 = 135;
double stopLoss = 0.0058;
int candlesInWave = 15;



MqlRates rawRates[];
double rates[100];


double fibo (double a, double b, double level){
        return b + level/100*(a-b);
       }


//reverse order of an array
void reverse(double &array[]){
   int size = ArraySize(array);
   double newArray[];
   ArrayResize(newArray, size);
   for(int i = 0; i < size; ++i){
      newArray[i] = array[i];
   }
   
   for(int i = 0; i < size; ++i){
      array[i] = newArray[size-i-1];
   }
   ArrayFree(newArray);
}


void sma(double &inp[], double &output[], int period){
   int size = ArraySize(inp);
   if (size < period){
      Print("SMA calculation error: not enough input");
      return;
   }
   
   
   ArrayResize(output, size-period+1);
   double sum = 0;
   for(int i = 0; i < period; ++i){
      sum += inp[i];
   }
   
   
   for(int i = 0; i < size-period+1; ++i){
      output[i] = sum/period;
      if(i != size-period){
         sum += inp[i+period];
         sum -= inp[i];}
   }
   ArraySetAsSeries(output, true);
}


void ema(double &inp[], double &output[], int period){
   double sc = 2/((double)period+1);
   int size = ArraySize(inp);
   
   
   ArrayResize(output, size-period+1);
   
   double sum = 0;
   for(int i = 0; i < period; ++i){
      sum += inp[i];
   }
   output[0] = sum/period;
   
   
   for(int i = 1; i < size-period+1; ++i){
      output[i] = (inp[i+period-1] - output[i-1]) * sc + output[i-1]; 
   }
   ArraySetAsSeries(output, true);
}
   
   
int OnInit()
  {
  

  
  
   return(INIT_SUCCEEDED);
  }
//+------------------------------------------------------------------+
//| Expert deinitialization function                                 |
//+------------------------------------------------------------------+
void OnDeinit(const int reason)
  {
//---
   
  }
//+------------------------------------------------------------------+
//| Expert tick function                                             |
//+------------------------------------------------------------------+
void OnTick()
  {
//---

   ArraySetAsSeries(rawRates,true);
   CopyRates("EURUSD",PERIOD_M1,0,Bars("EURUSD",PERIOD_M1),rawRates); // Copied all datas
   
   datetime t1 = rates[0].time;
   MqlDateTime x;
   TimeToStruct(t1, x);
   if(((x.day_of_week-1)*24*60 + (x.hour-1)*60 + x.min)%97 == 1){
   
   
      int j = 0;
      for(int i = 1; i < period*100; i += period){
         rates[j] = rawRates[i].close;
         ++j;
      }
      
      double r_rates[100];
      ArrayCopy(r_rates, rates);
      reverse(r_rates);
      
      double ema[], fsma[], ssma[];
      ema(r_rates, ema, ema_period);
      sma(r_rates, fsma, fsma_period);
      sma(r_rates, ssma, ssma_period);*/
   
   
   
   }
  }
//+------------------------------------------------------------------+
