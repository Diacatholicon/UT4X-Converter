/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xtx.ut4converter.t3d;

import org.xtx.ut4converter.MapConverter;
import org.xtx.ut4converter.export.UTPackageExtractor;
import org.xtx.ut4converter.ucore.UPackageRessource;

/**
 * Class for converting any actor related to sound (might be music as well)
 * TODO merge with T3D Actor and delete this class
 * because any actors can have sound property
 * @author XtremeXp
 */
public class T3DSound extends T3DActor {

    /**
     * UE1, UE4
     */
    UPackageRessource ambientSound;
    
    /**
     * UE1, not UE4
     */
    Double soundRadius;
    
    /**
     * UE1 (default: 190 max 255),
     * UE4
     */
    Double soundVolume;
    
    /**
     * UE1, UE4
     */
    Double soundPitch;
    
    /**
     *
     * @param mc
     */
    public T3DSound(MapConverter mc) {
        super(mc);
        ue4RootCompType = T3DMatch.UE4_RCType.AUDIO;
    }
    
    @Override
    public boolean analyseT3DData(String line) {
        
        if(line.contains("SoundRadius")){
            soundRadius = T3DUtils.getDouble(line);
        }
        
        else if(line.contains("SoundVolume")){
            soundVolume = T3DUtils.getDouble(line);
        }
        
        else if(line.contains("SoundPitch")){
            soundPitch = T3DUtils.getDouble(line);
        }
        
        // AmbientSound=Sound'AmbAncient.Looping.Stower51'
        else if(line.contains("AmbientSound=")){
            ambientSound = getUPackageRessource(line.split("\\'")[1], T3DRessource.Type.SOUND);
        } 
        else {
            return super.analyseT3DData(line);
        }
        
        return true;
    }
    
    @Override
    public void scale(Double newScale){
        
        if(soundRadius != null){
            soundRadius *= newScale;
        }
        
        super.scale(newScale);
    }
    
    /**
     *
     */
    @Override
    public void convert(){
        
        if(mapConverter.isFromUE1UE2ToUE3UE4()){
            if(soundVolume != null){
               soundVolume /= 255D;
            }
        }
        
        if(mapConverter.convertSounds && ambientSound != null){
            ambientSound.export(UTPackageExtractor.getExtractor(mapConverter, null));
        }
        
        super.convert();
    }
    
    /**
     *
     * @return
     */
    @Override
    public String toString(){
        
        if(ambientSound == null){
            return super.toString();
        }
        
        if(mapConverter.toUnrealEngine4()){
            
            if(!name.contains("Sound")){
                name += "Sound";
            }
            
            sbf.append(IDT).append("Begin Actor Class=AmbientSound Name=").append(name).append("\n");
            sbf.append(IDT).append("\tBegin Object Class=AudioComponent Name=\"AudioComponent0\"\n");
            sbf.append(IDT).append("\tEnd Object\n");
            sbf.append(IDT).append("\tBegin Object Name=\"AudioComponent0\"\n");
            
            if(ambientSound != null){
                sbf.append(IDT).append("\t\tSound=SoundCue'").append(ambientSound.getConvertedName(mapConverter)).append("'\n");
            }
            
            //bOverrideAttenuation=True
            if(soundVolume != null){
                sbf.append(IDT).append("\t\tVolumeMultiplier=").append(soundVolume).append("\n");
            }
            
            if(soundPitch != null){
                sbf.append(IDT).append("\t\tPitchMultiplier=").append(soundPitch).append("\n");
            }
            writeLocRotAndScale();
            sbf.append(IDT).append("\tEnd Object\n");
            sbf.append(IDT).append("\tAudioComponent=AudioComponent0\n");
            sbf.append(IDT).append("\tRootComponent=AudioComponent0\n");
            writeEndActor();
        }
        
        
        return super.toString();
    }
}
