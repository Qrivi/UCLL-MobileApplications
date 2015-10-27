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
		TimeSpan accumulatedTime;

		enum State{ Stopped, Started, Paused }
		State currentState;

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
			currentState = State.Stopped;

			timeHandler = new Handler ();
			startTime = stopTime = new DateTime ();
			accumulatedTime = new TimeSpan ();
		}

		protected override void OnResume(){
			base.OnResume ();

			currentState = (State)data.GetInt ("CurrentState", 0);
			startTime = DateTime.Parse( data.GetString("StartTime", startTime.ToString()) );
			stopTime = DateTime.Parse( data.GetString("StopTime", stopTime.ToString()) );
			timeView.Text = data.GetString ("Counter", "00:00.000");

			switch (currentState) {
			case State.Started:
				startStopButton.Text = GetString (Resource.String.stop);
				pauseButton.Enabled = true;
				resetButton.Enabled = false;

				TriggerUpdate ();
				break;
			case State.Paused:
				pauseButton.Enabled = false;
				resetButton.Enabled = false;
				break;
			}
		}

		protected override void OnPause(){
			base.OnPause ();

			editor.PutInt ("CurrentState", (int)currentState );
			editor.PutString ("StartTime", startTime.ToString() );
			editor.PutString ("StopTime", stopTime.ToString() );
			editor.PutString ("Counter", timeView.Text);
			editor.Apply ();
		}

		/* 
		protected override void OnSaveInstanceState(Bundle outState) {
			base.OnSaveInstanceState (outState);
			outState.PutInt ("count", count );

		}

		protected override void OnRestoreInstanceState (Bundle savedInstanceState) {
			base.OnRestoreInstanceState (savedInstanceState);
			count = (int) savedInstanceState.Get ("count");
			ShowCount ();
		}
		*/

		private void ResetHandler(){
			startTime = new DateTime();
			stopTime = new DateTime();
			accumulatedTime = new TimeSpan ();
			timeView.Text = accumulatedTime.ToString(@"mm\:ss\.fff");
		}

		private void PauseHandler(){
			currentState = State.Paused;
			pauseButton.Enabled = false;
			resetButton.Enabled = false;
			startStopButton.Text = GetString (Resource.String.start);
		}

		private void StartStopHandler(){
			switch (currentState) {
			case State.Stopped:
			case State.Paused:
				currentState = State.Started;
				pauseButton.Enabled = true;
				resetButton.Enabled = false;
				startStopButton.Text = GetString (Resource.String.stop);

				if (startTime == DateTime.MinValue) {
					startTime = DateTime.Now;
				}else if (stopTime != DateTime.MinValue) {
					startTime = startTime.Add( DateTime.Now.Subtract (stopTime) );
				}

				TriggerUpdate ();
				break;
			case State.Started:
				currentState = State.Stopped;
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

			if(currentState == State.Started) TriggerUpdate ();
		}
	}
}
