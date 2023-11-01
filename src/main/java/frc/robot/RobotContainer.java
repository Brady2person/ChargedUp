// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.*;
//import frc.robot.commands.arm.TeleopInnerArm2;
//import frc.robot.commands.arm.TeleopInnerarm2;
import frc.robot.subsystems.*;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.Constants.OperatorConstants;
import javax.lang.model.util.ElementScanner14;
import javax.swing.GroupLayout.SequentialGroup;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in
 * the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of
 * the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
    // creates auto command groups

    // creates sendable chooser object!
    // private SendableChooser<Command> autochooser = new SendableChooser<>();

    public static final SwerveDrive Drive = new SwerveDrive();

    // The robot's subsystems and commands are defined here...
    private final XboxController primaryDriver = new XboxController(0);
    private final Joystick overRideLeft = new Joystick(1);
    private final Joystick overRideRight = new Joystick(2);

    /* Joystick and Controller assignments */
    /* Drive Controls - Xbox Controller */
    private final int translationAxis = XboxController.Axis.kLeftY.value;
    private final int strafeAxis = XboxController.Axis.kLeftX.value;
    private final int rotationAxis = XboxController.Axis.kRightX.value;

    //
    /**************/
    /* Driver Buttons - Xbox Controller */
    // ABXY buttons
    private final JoystickButton eTeir = new JoystickButton(
            primaryDriver,
            XboxController.Button.kA.value); // A - Traversal mode - eTeir
    private final JoystickButton midTier = new JoystickButton(
            primaryDriver,
            XboxController.Button.kB.value); // B - Middle shelf position
    private final JoystickButton sTeir = new JoystickButton(
            primaryDriver,
            XboxController.Button.kY.value); // Y - Top shelf position
    private final JoystickButton home = new JoystickButton(
            primaryDriver,
            XboxController.Button.kX.value); // X - Home position
    private final JoystickButton turtleMode = new JoystickButton(
            primaryDriver,
            XboxController.Button.kStart.value); // start button - Turtle Mode
    private final JoystickButton zeroGyro = new JoystickButton(
            primaryDriver,
            XboxController.Button.kBack.value); // BACK/SELECT - Zero Gyro reading
    // Bumpers and Triggers
    private final JoystickButton openClaw = new JoystickButton(
            primaryDriver,
            XboxController.Button.kRightBumper.value); // Left Bumper - auto-load
    private final JoystickButton closeClaw = new JoystickButton(
            primaryDriver,
            XboxController.Button.kLeftBumper.value); // Right bumper - Open/Close the claw

    // TODO this is assigned to Start but not invoked
    // private final JoystickButton centricToggle = new
    // JoystickButton(primaryDriver, XboxController.Button.kX.value);
    // private final JoystickButton autoLevel = new JoystickButton(primaryDriver,
    // XboxController.Button.kStart.value);
    // private final JoystickButton autoLoad = new JoystickButton(primaryDriver,
    // XboxController.Button.kLeftBumper.value);

    /**************/
    /* Co-Driver Buttons - Dual Joysticks */
    // INNER ARM - LEFT JOYSTICK
    private int innerArmAxis = Joystick.AxisType.kY.value;
    // private final JoystickButton innerRaise = new JoystickButton(overRideLeft,
    // Constants.InnerArmConstants.overrideUp); //Axis Back - Inner arm up
    // private final JoystickButton innerLower = new JoystickButton(overRideLeft,
    // Constants.InnerArmConstants.overrideDown); //Axis Forward - Inner arm down
    // LEFT JOYSTICK BUTTONS
    private final JoystickButton CoDriverETeir = new JoystickButton(
            overRideLeft,
            Constants.InnerArmConstants.overrideUp); // Left Top Front button
    private final JoystickButton CoDriverHome = new JoystickButton(
            overRideLeft,
            Constants.InnerArmConstants.overrideDown); // Left Top Rear button
    private final JoystickButton CoDriverH = new JoystickButton(
            overRideLeft,
            Constants.InnerArmConstants.overrideHuman); // Left Top Rear button
    /*************/
    // OUTER ARM - RIGHT JOYSTICK
    private int outerArmAxis = Joystick.AxisType.kY.value;
    // private final JoystickButton outerRaise = new JoystickButton(overRideRight,
    // Constants.OuterArmConstants.overrideUp); //Axis Back - Outer arm up
    // private final JoystickButton outerLower = new JoystickButton(overRideRight,
    // Constants.OuterArmConstants.overrideDown); //Axis Forward - Outer arm Down
    // RIGHT JOYSTICK BUTTONS
    private final JoystickButton CoDrivereSTeir = new JoystickButton(
            overRideRight,
            Constants.OuterArmConstants.LJSTopButton); // Right top Front button
    private final JoystickButton CoDrivereMidTeir = new JoystickButton(
            overRideRight,
            Constants.OuterArmConstants.LJSBottomButton); // Right top Rear button
    // CLAW - Joystics

    /* End of Joystick and Controller assignments */

    // triggers
    // Trigger whiskerTriggerCheck = new Trigger(() ->
    // m_Claw.whiskerSwitchClosed()); // could be used later

    /* Subsystems */
    private final SwerveDrive s_Swerve = new SwerveDrive();
    private int automode = 3;

    // 0 is side auto, 1 is mid auto, you can replace as need be but it's a bodgey
    // fix.
    // while yes a bool could be possible I sure freaking hope we cna get more than
    // 2
    /**
     * The container for the robot. Contains subsystems, OI devices, and commands.
     */
    public RobotContainer() {

        // SmartDashboard.putData("auto", autochooser);
        // autochooser.setDefaultOption("SideAuto", m_SideAuto);
        // autochooser.addOption("Mid auto", m_MiddleAuto);

        // XBOX CONTROLLER
        s_Swerve.setDefaultCommand(
                new TeleopSwerve(
                        s_Swerve,
                        () -> -primaryDriver.getRawAxis(translationAxis),
                        () -> -primaryDriver.getRawAxis(strafeAxis),
                        () -> -primaryDriver.getRawAxis(rotationAxis),
                        () -> zeroGyro.getAsBoolean()));

        // JOYSTICKS
        /*
         * m_InnerArm.setDefaultCommand(
         * new TeleopInnerArm2(m_InnerArm,
         * () -> overRideRight.getRawAxis(innerArmAxis))
         * );
         */

        // Configure the button bindings

        configureButtonBindings();
    }

    /* End Subsystems */

    /* MainDriver */
    /* Button Bindings - Actions taken upon button press or hold */
    private void configureButtonBindings() {
        // ZERO GYRO
        zeroGyro.onTrue(new SequentialCommandGroup(
                new InstantCommand(() -> s_Swerve.zeroGyro())));

        // new GreenLedON(m_LEDs).withTimeout(.5),
        ////// new GreenLedOFF(m_LEDs),
        // new WaitCommand(1),
        // new WhiteLedON(m_LEDs).withTimeout(.5),
        // new WhiteLedOFF(m_LEDs)
        // TURTLE
        turtleMode.onTrue(
                new InstantCommand(() -> s_Swerve.turtleMode()));
        // HOME
        // TRAVERSAL

        // HUMAN PLAYER / MID NODE
        // TOP NODE
        // CLAW
        /*
         * openClaw.onTrue(
         * new ParallelCommandGroup(
         * new OpenClaw(m_Claw),
         * new GreenLedOFF(m_LEDs),
         * new WhiteLedON(m_LEDs)
         * ));
         * 
         * closeClaw
         * .and(innerStowedCheck)
         * .and(outerStowedCheck)
         * .onTrue(
         * new SequentialCommandGroup(
         * new CloseClaw(m_Claw),
         * new OuterArmTravel(m_OuterArm),
         * new WhiteLedOFF(m_LEDs),
         * new GreenLedON(m_LEDs))
         * );
         * closeClaw
         * .and(innerStowedCheck.negate().or(outerStowedCheck.negate()))
         * .whileTrue(
         * new ParallelCommandGroup(
         * new CloseClaw(m_Claw),
         * new WhiteLedOFF(m_LEDs),
         * new GreenLedON(m_LEDs))
         * );
         */
        // These commands were previously tied to digital motion of the arm with the
        // Joystick buttons
        // outerRaise.whileTrue(new OuterArmRaise(m_OuterArm));
        // outerLower.whileTrue(new OuterArmLower(m_OuterArm));
        // innerRaise.whileTrue(new InnerArmRaise(m_InnerArm));
        // innerLower.whileTrue(new InnerArmLower(m_InnerArm));

        /* CoDriver */
        /* Button Bindings - Actions taken upon button press or hold */
        // HE
        // CLAW


        // Trigger lasersense = new Trigger(m_Claw::getLazerSenser);
        // lasersense.onFalse(new CloseClaw(m_Claw));


    }

    /**
     * Use this method to define your trigger->command mappings. Triggers can be
     * created via the
     * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with
     * an arbitrary
     * predicate, or via the named factories in {@link
     * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for
     * {@link
     * CommandXboxController
     * Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
     * PS4} controllers or
     * {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
     * joysticks}.
     */

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand() {
        // 0 is side auto, 1 is mid auto
        return new SequentialCommandGroup(
         new InstantCommand(() -> s_Swerve.zeroGyro()));
         /* //TIMEOUT IS NOT WORKING, BODGEEEE
         * new TeleopSwerve(s_Swerve, () -> -3, () -> 0.0, () -> 0.0 , () ->
         * false).withTimeout(.25),
         * //, new WaitCommand(.25)).withTimeout(.25),
         * // because it's allready in a scg we don't need to make a new one
         * new InnerArmRaiseM(m_InnerArm),
         * new OuterArmRaiseM(m_OuterArm),
         * new TeleopSwerve(s_Swerve, () -> 3, () -> 0.0, () -> 0.0 , () ->
         * false).withTimeout(.25),
         * // , new WaitCommand(0.5)).withTimeout(.5),
         * new OpenClaw(m_Claw),
         * new TeleopSwerve(s_Swerve,() -> -5.0,() -> 0.0,() ->0.0, () ->
         * false).withTimeout(.25),
         * // , new WaitCommand(0.55)).withTimeout(0.55),
         * new SequentialCommandGroup(
         * // as opposed to here where we do.
         * new WaitCommand(0.2),
         * new InnerArmStowed(m_InnerArm),
         * new OuterArmStowed(m_OuterArm)
         * ),
         * new TeleopSwerve(s_Swerve,() -> -5.0,() -> 0.0,() ->0.0, () ->
         * false).withTimeout(.25),
         * //new WaitCommand(1)).withTimeout(1.00),
         * new TeleopSwerve(s_Swerve, () -> 0, () -> 0, () -> .5, () ->
         * false).withTimeout(.25),
         * //new WaitCommand(0.5)).withTimeout(0.5),
         * new InstantCommand(() -> s_Swerve.zeroGyro()), // 180 the Gyro
         * new TeleopSwerve(s_Swerve, () -> 0, () -> 0, () -> .25, () ->
         * false).withTimeout(.25)
         * //new WaitCommand(0.25)).withTimeout(0.25)
         * );}
         * /* return new SequentialCommandGroup(
         * new InstantCommand(() -> s_Swerve.zeroGyro()),
         * //TIMEOUT IS NOT WORKING, BODGEEEE
         * new ParallelRaceGroup(new TeleopSwerve(s_Swerve, () -> -3, () -> 0.0, () ->
         * 0.0 , () -> false), new WaitCommand(.25)).withTimeout(.25),
         * // because it's allready in a scg we don't need to make a new one
         * new InnerArmRaiseM(m_InnerArm),
         * new OuterArmRaiseM(m_OuterArm),
         * new ParallelRaceGroup(new TeleopSwerve(s_Swerve, () -> 3, () -> 0.0, () ->
         * 0.0 , () -> false), new WaitCommand(0.5)).withTimeout(.5),
         * new OpenClaw(m_Claw),
         * new ParallelRaceGroup( new TeleopSwerve(s_Swerve,() -> -5.0,() -> 0.0,()
         * ->0.0, () -> false), new WaitCommand(0.55)).withTimeout(0.55),
         * new SequentialCommandGroup(
         * // as opposed to here where we do.
         * new WaitCommand(0.2),
         * new InnerArmStowed(m_InnerArm),
         * new OuterArmStowed(m_OuterArm)
         * ),
         * new ParallelRaceGroup(new TeleopSwerve(s_Swerve,() -> -5.0,() -> 0.0,()
         * ->0.0, () -> false), new WaitCommand(1)).withTimeout(1.00),
         * new ParallelRaceGroup(new TeleopSwerve(s_Swerve, () -> 0, () -> 0, () -> .5,
         * () -> false), new WaitCommand(0.5)).withTimeout(0.5),
         * new InstantCommand(() -> s_Swerve.zeroGyro()), // 180 the Gyro
         * new ParallelRaceGroup(new TeleopSwerve(s_Swerve, () -> 0, () -> 0, () -> .25,
         * () -> false), new WaitCommand(0.25)).withTimeout(0.25)
         * );}
         */

        // else if(s_Swerve.automode == 1){
        // return new SequentialCommandGroup(
        /*
         * new InstantCommand(() -> s_Swerve.zeroGyro()),
         * new TeleopSwerve(s_Swerve, () -> -3, () -> 0.0, () -> 0.0 , () ->
         * false).withTimeout(.25),
         * //new TeleopSwerve(s_Swerve, () -> -3, () -> 0.0, () -> 0.0 , () ->
         * false).withTimeout(.2),
         * // because it's allready in a scg we don't need to make a new one
         * new InnerArmRaiseM(m_InnerArm),
         * new OuterArmRaiseM(m_OuterArm),
         * new TeleopSwerve(s_Swerve, () -> 3, () -> 0.0, () -> 0.0 , () ->
         * false).withTimeout(0.5),
         * new OpenClaw(m_Claw),
         * new TeleopSwerve(s_Swerve,() -> -5.0,() -> 0.0,() ->0.0, () ->
         * false).withTimeout(0.2),
         * new SequentialCommandGroup(
         * // as opposed to here where we do.
         * new WaitCommand(0.2),
         * new InnerArmStowed(m_InnerArm),
         * new OuterArmStowed(m_OuterArm)
         * ),
         * new TeleopSwerve(s_Swerve,() -> -5.0,() -> 0.0,() ->0.0, () ->
         * false).withTimeout(2.3),
         * new TeleopSwerve(s_Swerve, () -> 0, () -> 0, () -> .5, () ->
         * false).withTimeout(0.5),
         * new InstantCommand(() -> s_Swerve.zeroGyro()), // 180 the Gyro
         * new WaitCommand(2.7),
         * new TeleopSwerve(s_Swerve, () -> -5.0, () -> 0, () -> 0, () ->
         * false).withTimeout(0.55),
         * new TeleopSwerve(s_Swerve, () -> 0, () -> 0, () -> .2, () ->
         * false).withTimeout(0.5));
         * }
         * // else {
         * // return new WaitCommand(1);
         */
        // because it's allready in a scg we don't need to make a new one
    }

    }
// return autochooser.getSelected();
