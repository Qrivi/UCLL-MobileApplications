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
	[Activity (Label = "@string/app_name", MainLauncher = true, Icon = "@drawable/icon")]
	public class MainActivity : Activity
	{
		
		TextView timeView;
		Button startStopButton,
			pauseButton,
			resetButton;

		ISharedPreferences data;
		ISharedPreferencesEditor editor;

		Handler timeHandler;
		DateTime startTime, 
			stopTime;
		TimeSpan accumulatedTime,
			accumulatedStoppedTime;

		int state = 0; 	
		// 0: stopped
		// 1: started
		// 2: paused

		protected override void OnCreate (Bundle bundle)
		{
			base.OnCreate (bundle);

			Log.Info ("Chronometer", "Hello World");

			// Create preference file and editor
			data = Application.Context.GetSharedPreferences("Data", FileCreationMode.Private);
			editor = data.Edit ();

			// Set our view from the "main" layout resource
			SetContentView (Resource.Layout.Main);

			// Get our button from the layout resource,
			// and attach an event to it
			timeView = FindViewById<TextView> (Resource.Id.timeView);
			startStopButton = FindViewById<Button> (Resource.Id.startStopButton);
			pauseButton = FindViewById<Button> (Resource.Id.pauseButton);
			resetButton = FindViewById<Button> (Resource.Id.resetButton);

			startStopButton.Click += (sender, e) => StartStopHandler();
			pauseButton.Click += (sender, e) => PauseHandler();
			resetButton.Click += (sender, e) => ResetHandler();

			pauseButton.Enabled = false;

			timeHandler = new Handler ();
			startTime = stopTime = new DateTime ();
			accumulatedTime = accumulatedStoppedTime = new TimeSpan ();
		}

		protected override void OnResume(){
			base.OnResume ();

			//accumulatedTime = TimeSpan.Parse(data.GetString ("count", "0" ));
			//updateTimeView ();
		}

		protected override void OnPause(){
			base.OnPause ();

			//isRunning = true;
			//StartStopHandler ();

			//editor.PutString ("count", accumulatedTime.ToString() );
			//editor.Apply ();
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

		private void ResetHandler(){
			startTime = new DateTime();
			accumulatedTime = new TimeSpan ();
			timeView.Text = accumulatedTime.ToString(@"mm\:ss\.fff");
		}

		private void PauseHandler(){
			state = 2; // now paused
			pauseButton.Enabled = false;
			resetButton.Enabled = false;
			startStopButton.Text = GetString (Resource.String.start);
		}

		private void StartStopHandler(){
			switch (state) {
			case 0:
			case 2:
				state = 1; // now resumed/started
				pauseButton.Enabled = true;
				resetButton.Enabled = false;
				startStopButton.Text = GetString (Resource.String.stop);

				if (startTime == DateTime.MinValue)
					startTime = DateTime.Now;
				if (stopTime != DateTime.MinValue) {
					startTime = startTime.Add( DateTime.Now.Subtract (stopTime) );
					accumulatedTime = accumulatedTime.Subtract (accumulatedStoppedTime);
					stopTime = new DateTime();
				}

				TriggerUpdate ();
				break;
			case 1:
				state = 0; // now stopped
				pauseButton.Enabled = false;
				resetButton.Enabled = true;
				startStopButton.Text = GetString (Resource.String.start);

				stopTime = DateTime.Now;
				break;
			}
		}

		private void TriggerUpdate(){
			timeHandler.PostDelayed (Update, 1);
		}
		private void Update(){
			accumulatedTime = DateTime.Now.Subtract( startTime );
			timeView.Text = accumulatedTime.ToString(@"mm\:ss\.fff");

			if(state == 1) TriggerUpdate ();
		}
	}
}
