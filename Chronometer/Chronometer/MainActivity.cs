using System;

using Android.App;
using Android.Content;
using Android.Runtime;
using Android.Views;
using Android.Widget;
using Android.OS;
using Android.Util;

namespace Chronometer
{
	[Activity (Label = "Chronometer", MainLauncher = true, Icon = "@drawable/icon")]
	public class MainActivity : Activity
	{
		
		TextView timeView;
		Button startStopButton;
		Button resetButton;

		ISharedPreferences data;
		ISharedPreferencesEditor editor;

		Handler timeHandler;
		int count = 0;
		bool isRunning = false;

		protected override void OnCreate (Bundle bundle)
		{

			base.OnCreate (bundle);
			Log.Info ("Chronometer", "Hello World");

			// Set our view from the "main" layout resource
			SetContentView (Resource.Layout.Main);
			data = Application.Context.GetSharedPreferences("Data", FileCreationMode.Private);
			editor = data.Edit ();

			// Get our button from the layout resource,
			// and attach an event to it
			timeView = FindViewById<TextView> (Resource.Id.timeView);
			startStopButton = FindViewById<Button> (Resource.Id.startStopButton);
			resetButton = FindViewById<Button> (Resource.Id.resetButton);

			timeView.Text = "0:00:00";
			startStopButton.Text = "Start";
			
			startStopButton.Click += (sender, e) => StartStopHandler();
			resetButton.Click += (sender, e) => ResetHandler();

			timeHandler = new Handler ();
		}

		protected override void OnResume(){
			base.OnResume ();
			count = data.GetInt ("Count", 0 );
			ShowCount ();
		}

		protected override void OnPause(){
			base.OnPause ();
			editor.PutInt ("Count", count );
			editor.Apply ();

			//Log.Info ("yolo", count.ToString() );
			//Log.Info ( "swag", data.GetInt ("Count", 69).ToString() );
		}

		/* protected override void OnSaveInstanceState(Bundle outState) {
			base.OnSaveInstanceState (outState);
			outState.PutInt ("count", count );

		}

		protected override void OnRestoreInstanceState (Bundle savedInstanceState) {
			base.OnRestoreInstanceState (savedInstanceState);
			count = (int) savedInstanceState.Get ("count");
			ShowCount ();
		}*/

		private void ShowCount(){
			timeView.Text = count.ToString();
		}

		private void Increment(){
			count++;
			ShowCount();
		}

		private void ResetHandler(){
			count = 0;
			ShowCount ();
		}

		private void StartStopHandler(){
			Log.Info ("Chronometer", "startStopHandler()");

			if (!isRunning) {
				startStopButton.Text = "Stop";
				isRunning = true;
				GenerateDelayedTick ();
			} else {
				isRunning = false;
				startStopButton.Text = "Start";
			}

				
			//Increment ();
		}

		private void GenerateDelayedTick(){
			if(isRunning) timeHandler.PostDelayed (OnTick, 1);
		}
		private void OnTick(){
			Increment ();
			GenerateDelayedTick ();
		}
	}
}
