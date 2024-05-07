// Copyright 2018 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.location.custom_suplclient.asn1.supl2.lpp;

// Copyright 2008 Google Inc. All Rights Reserved.
/*
 * This class is AUTOMATICALLY GENERATED. Do NOT EDIT.
 */


//
//
import com.google.location.custom_suplclient.asn1.base.Asn1Enumerated;
import com.google.location.custom_suplclient.asn1.base.Asn1Null;
import com.google.location.custom_suplclient.asn1.base.Asn1Object;
import com.google.location.custom_suplclient.asn1.base.Asn1Sequence;
import com.google.location.custom_suplclient.asn1.base.Asn1Tag;
import com.google.location.custom_suplclient.asn1.base.BitStream;
import com.google.location.custom_suplclient.asn1.base.BitStreamReader;
import com.google.location.custom_suplclient.asn1.base.SequenceComponent;
import com.google.common.collect.ImmutableList;
import java.util.Collection;
import javax.annotation.Nullable;


/**
* 
*/
public  class WLAN_TargetDeviceErrorCauses_r13 extends Asn1Sequence {
  //

  private static final Asn1Tag TAG_WLAN_TargetDeviceErrorCauses_r13
      = Asn1Tag.fromClassAndNumber(-1, -1);

  public WLAN_TargetDeviceErrorCauses_r13() {
    super();
  }

  @Override
  @Nullable
  protected Asn1Tag getTag() {
    return TAG_WLAN_TargetDeviceErrorCauses_r13;
  }

  @Override
  protected boolean isTagImplicit() {
    return true;
  }

  public static Collection<Asn1Tag> getPossibleFirstTags() {
    if (TAG_WLAN_TargetDeviceErrorCauses_r13 != null) {
      return ImmutableList.of(TAG_WLAN_TargetDeviceErrorCauses_r13);
    } else {
      return Asn1Sequence.getPossibleFirstTags();
    }
  }

  /**
   * Creates a new WLAN_TargetDeviceErrorCauses_r13 from encoded stream.
   */
  public static WLAN_TargetDeviceErrorCauses_r13 fromPerUnaligned(byte[] encodedBytes) {
    WLAN_TargetDeviceErrorCauses_r13 result = new WLAN_TargetDeviceErrorCauses_r13();
    result.decodePerUnaligned(new BitStreamReader(encodedBytes));
    return result;
  }

  /**
   * Creates a new WLAN_TargetDeviceErrorCauses_r13 from encoded stream.
   */
  public static WLAN_TargetDeviceErrorCauses_r13 fromPerAligned(byte[] encodedBytes) {
    WLAN_TargetDeviceErrorCauses_r13 result = new WLAN_TargetDeviceErrorCauses_r13();
    result.decodePerAligned(new BitStreamReader(encodedBytes));
    return result;
  }



  @Override protected boolean isExtensible() {
    return true;
  }

  @Override public boolean containsExtensionValues() {
    for (SequenceComponent extensionComponent : getExtensionComponents()) {
      if (extensionComponent.isExplicitlySet()) return true;
    }
    return false;
  }

  
  private WLAN_TargetDeviceErrorCauses_r13.cause_r13Type cause_r13_;
  public WLAN_TargetDeviceErrorCauses_r13.cause_r13Type getCause_r13() {
    return cause_r13_;
  }
  /**
   * @throws ClassCastException if value is not a WLAN_TargetDeviceErrorCauses_r13.cause_r13Type
   */
  public void setCause_r13(Asn1Object value) {
    this.cause_r13_ = (WLAN_TargetDeviceErrorCauses_r13.cause_r13Type) value;
  }
  public WLAN_TargetDeviceErrorCauses_r13.cause_r13Type setCause_r13ToNewInstance() {
    cause_r13_ = new WLAN_TargetDeviceErrorCauses_r13.cause_r13Type();
    return cause_r13_;
  }
  
  private WLAN_TargetDeviceErrorCauses_r13.wlan_AP_RSSI_MeasurementNotPossible_r13Type wlan_AP_RSSI_MeasurementNotPossible_r13_;
  public WLAN_TargetDeviceErrorCauses_r13.wlan_AP_RSSI_MeasurementNotPossible_r13Type getWlan_AP_RSSI_MeasurementNotPossible_r13() {
    return wlan_AP_RSSI_MeasurementNotPossible_r13_;
  }
  /**
   * @throws ClassCastException if value is not a WLAN_TargetDeviceErrorCauses_r13.wlan_AP_RSSI_MeasurementNotPossible_r13Type
   */
  public void setWlan_AP_RSSI_MeasurementNotPossible_r13(Asn1Object value) {
    this.wlan_AP_RSSI_MeasurementNotPossible_r13_ = (WLAN_TargetDeviceErrorCauses_r13.wlan_AP_RSSI_MeasurementNotPossible_r13Type) value;
  }
  public WLAN_TargetDeviceErrorCauses_r13.wlan_AP_RSSI_MeasurementNotPossible_r13Type setWlan_AP_RSSI_MeasurementNotPossible_r13ToNewInstance() {
    wlan_AP_RSSI_MeasurementNotPossible_r13_ = new WLAN_TargetDeviceErrorCauses_r13.wlan_AP_RSSI_MeasurementNotPossible_r13Type();
    return wlan_AP_RSSI_MeasurementNotPossible_r13_;
  }
  
  private WLAN_TargetDeviceErrorCauses_r13.wlan_AP_RTT_MeasurementNotPossible_r13Type wlan_AP_RTT_MeasurementNotPossible_r13_;
  public WLAN_TargetDeviceErrorCauses_r13.wlan_AP_RTT_MeasurementNotPossible_r13Type getWlan_AP_RTT_MeasurementNotPossible_r13() {
    return wlan_AP_RTT_MeasurementNotPossible_r13_;
  }
  /**
   * @throws ClassCastException if value is not a WLAN_TargetDeviceErrorCauses_r13.wlan_AP_RTT_MeasurementNotPossible_r13Type
   */
  public void setWlan_AP_RTT_MeasurementNotPossible_r13(Asn1Object value) {
    this.wlan_AP_RTT_MeasurementNotPossible_r13_ = (WLAN_TargetDeviceErrorCauses_r13.wlan_AP_RTT_MeasurementNotPossible_r13Type) value;
  }
  public WLAN_TargetDeviceErrorCauses_r13.wlan_AP_RTT_MeasurementNotPossible_r13Type setWlan_AP_RTT_MeasurementNotPossible_r13ToNewInstance() {
    wlan_AP_RTT_MeasurementNotPossible_r13_ = new WLAN_TargetDeviceErrorCauses_r13.wlan_AP_RTT_MeasurementNotPossible_r13Type();
    return wlan_AP_RTT_MeasurementNotPossible_r13_;
  }
  

  

  

  @Override public Iterable<? extends SequenceComponent> getComponents() {
    ImmutableList.Builder<SequenceComponent> builder = ImmutableList.builder();
    
    builder.add(new SequenceComponent() {
          Asn1Tag tag = Asn1Tag.fromClassAndNumber(2, 0);

          @Override public boolean isExplicitlySet() {
            return getCause_r13() != null;
          }

          @Override public boolean hasDefaultValue() {
            return false;
          }

          @Override public boolean isOptional() {
            return false;
          }

          @Override public Asn1Object getComponentValue() {
            return getCause_r13();
          }

          @Override public void setToNewInstance() {
            setCause_r13ToNewInstance();
          }

          @Override public Collection<Asn1Tag> getPossibleFirstTags() {
            return tag == null ? WLAN_TargetDeviceErrorCauses_r13.cause_r13Type.getPossibleFirstTags() : ImmutableList.of(tag);
          }

          @Override
          public Asn1Tag getTag() {
            return tag;
          }

          @Override
          public boolean isImplicitTagging() {
            return true;
          }

          @Override public String toIndentedString(String indent) {
                return "cause_r13 : "
                    + getCause_r13().toIndentedString(indent);
              }
        });
    
    builder.add(new SequenceComponent() {
          Asn1Tag tag = Asn1Tag.fromClassAndNumber(2, 1);

          @Override public boolean isExplicitlySet() {
            return getWlan_AP_RSSI_MeasurementNotPossible_r13() != null;
          }

          @Override public boolean hasDefaultValue() {
            return false;
          }

          @Override public boolean isOptional() {
            return true;
          }

          @Override public Asn1Object getComponentValue() {
            return getWlan_AP_RSSI_MeasurementNotPossible_r13();
          }

          @Override public void setToNewInstance() {
            setWlan_AP_RSSI_MeasurementNotPossible_r13ToNewInstance();
          }

          @Override public Collection<Asn1Tag> getPossibleFirstTags() {
            return tag == null ? WLAN_TargetDeviceErrorCauses_r13.wlan_AP_RSSI_MeasurementNotPossible_r13Type.getPossibleFirstTags() : ImmutableList.of(tag);
          }

          @Override
          public Asn1Tag getTag() {
            return tag;
          }

          @Override
          public boolean isImplicitTagging() {
            return true;
          }

          @Override public String toIndentedString(String indent) {
                return "wlan_AP_RSSI_MeasurementNotPossible_r13 : "
                    + getWlan_AP_RSSI_MeasurementNotPossible_r13().toIndentedString(indent);
              }
        });
    
    builder.add(new SequenceComponent() {
          Asn1Tag tag = Asn1Tag.fromClassAndNumber(2, 2);

          @Override public boolean isExplicitlySet() {
            return getWlan_AP_RTT_MeasurementNotPossible_r13() != null;
          }

          @Override public boolean hasDefaultValue() {
            return false;
          }

          @Override public boolean isOptional() {
            return true;
          }

          @Override public Asn1Object getComponentValue() {
            return getWlan_AP_RTT_MeasurementNotPossible_r13();
          }

          @Override public void setToNewInstance() {
            setWlan_AP_RTT_MeasurementNotPossible_r13ToNewInstance();
          }

          @Override public Collection<Asn1Tag> getPossibleFirstTags() {
            return tag == null ? WLAN_TargetDeviceErrorCauses_r13.wlan_AP_RTT_MeasurementNotPossible_r13Type.getPossibleFirstTags() : ImmutableList.of(tag);
          }

          @Override
          public Asn1Tag getTag() {
            return tag;
          }

          @Override
          public boolean isImplicitTagging() {
            return true;
          }

          @Override public String toIndentedString(String indent) {
                return "wlan_AP_RTT_MeasurementNotPossible_r13 : "
                    + getWlan_AP_RTT_MeasurementNotPossible_r13().toIndentedString(indent);
              }
        });
    
    return builder.build();
  }

  @Override public Iterable<? extends SequenceComponent>
                                                    getExtensionComponents() {
    ImmutableList.Builder<SequenceComponent> builder = ImmutableList.builder();
      
      return builder.build();
    }

  
  // Copyright 2008 Google Inc. All Rights Reserved.
/*
 * AUTOMATICALLY GENERATED. Do NOT EDIT.
 */


//

/**
 * 
 */
public static class cause_r13Type extends Asn1Enumerated {
  public enum Value implements Asn1Enumerated.Value {
    undefined(0),
    requestedMeasurementsNotAvailable(1),
    notAllrequestedMeasurementsPossible(2),
    ;

    Value(int i) {
      value = i;
    }

    private int value;
    public int getAssignedValue() {
      return value;
    }

    @Override public boolean isExtensionValue() {
      return false;
    }
  }

  @Override
  protected Value getDefaultValue() {
    return null
;
  }

  @SuppressWarnings("unchecked")
  public Value enumValue() {
    return (Value) getValue();
  }


  
  public void setTo_undefined() {
    setValue(Value.undefined);
  }
  
  public void setTo_requestedMeasurementsNotAvailable() {
    setValue(Value.requestedMeasurementsNotAvailable);
  }
  
  public void setTo_notAllrequestedMeasurementsPossible() {
    setValue(Value.notAllrequestedMeasurementsPossible);
  }
  


  public enum ExtensionValue implements Asn1Enumerated.Value {
    ;

    ExtensionValue(int i) {
      value = i;
    }

    private int value;
    @Override public int getAssignedValue() {
      return value;
    }

    @Override public boolean isExtensionValue() {
      return true;
    }
  }

  @SuppressWarnings("unchecked")
  public ExtensionValue extEnumValue() {
    return (ExtensionValue) getValue();
  }

  

  

  private static final Asn1Tag TAG_cause_r13Type
      = Asn1Tag.fromClassAndNumber(-1, -1);

  public cause_r13Type() {
    super();
    // use template substitution instead of calling getDefaultValue(), since
    // calling virtual methods from a ctor is frowned upon here.
    setValue(null
);
  }

  @Override
  @Nullable
  protected Asn1Tag getTag() {
    return TAG_cause_r13Type;
  }

  @Override
  protected boolean isTagImplicit() {
    return true;
  }

  public static Collection<Asn1Tag> getPossibleFirstTags() {
    if (TAG_cause_r13Type != null) {
      return ImmutableList.of(TAG_cause_r13Type);
    } else {
      return Asn1Enumerated.getPossibleFirstTags();
    }
  }

  @Override protected boolean isExtensible() {
    return true;
  }

  @Override protected Asn1Enumerated.Value lookupValue(int ordinal) {
    return Value.values()[ordinal];
  }

  @Override protected Asn1Enumerated.Value lookupExtensionValue(int ordinal) {
    return ExtensionValue.values()[ordinal];
  }

  @Override protected int getValueCount() {
    return Value.values().length;
  }

  /**
   * Creates a new cause_r13Type from encoded stream.
   */
  public static cause_r13Type fromPerUnaligned(byte[] encodedBytes) {
    cause_r13Type result = new cause_r13Type();
    result.decodePerUnaligned(new BitStreamReader(encodedBytes));
    return result;
  }

  /**
   * Creates a new cause_r13Type from encoded stream.
   */
  public static cause_r13Type fromPerAligned(byte[] encodedBytes) {
    cause_r13Type result = new cause_r13Type();
    result.decodePerAligned(new BitStreamReader(encodedBytes));
    return result;
  }

  @Override public Iterable<BitStream> encodePerUnaligned() {
    return super.encodePerUnaligned();
  }

  @Override public Iterable<BitStream> encodePerAligned() {
    return super.encodePerAligned();
  }

  @Override public void decodePerUnaligned(BitStreamReader reader) {
    super.decodePerUnaligned(reader);
  }

  @Override public void decodePerAligned(BitStreamReader reader) {
    super.decodePerAligned(reader);
  }

  @Override public String toString() {
    return toIndentedString("");
  }

  public String toIndentedString(String indent) {
    return "cause_r13Type = " + getValue() + ";\n";
  }
}

  
  // Copyright 2008 Google Inc. All Rights Reserved.
/*
 * This class is AUTOMATICALLY GENERATED. Do NOT EDIT.
 */


//

/**
 * 
 */
public static class wlan_AP_RSSI_MeasurementNotPossible_r13Type extends Asn1Null {
  //

  private static final Asn1Tag TAG_wlan_AP_RSSI_MeasurementNotPossible_r13Type
      = Asn1Tag.fromClassAndNumber(-1, -1);

  public wlan_AP_RSSI_MeasurementNotPossible_r13Type() {
    super();
  }

  @Override
  @Nullable
  protected Asn1Tag getTag() {
    return TAG_wlan_AP_RSSI_MeasurementNotPossible_r13Type;
  }

  @Override
  protected boolean isTagImplicit() {
    return true;
  }

  public static Collection<Asn1Tag> getPossibleFirstTags() {
    if (TAG_wlan_AP_RSSI_MeasurementNotPossible_r13Type != null) {
      return ImmutableList.of(TAG_wlan_AP_RSSI_MeasurementNotPossible_r13Type);
    } else {
      return Asn1Null.getPossibleFirstTags();
    }
  }

  /**
   * Creates a new wlan_AP_RSSI_MeasurementNotPossible_r13Type from encoded stream.
   */
  public static wlan_AP_RSSI_MeasurementNotPossible_r13Type fromPerUnaligned(byte[] encodedBytes) {
    wlan_AP_RSSI_MeasurementNotPossible_r13Type result = new wlan_AP_RSSI_MeasurementNotPossible_r13Type();
    result.decodePerUnaligned(new BitStreamReader(encodedBytes));
    return result;
  }

  /**
   * Creates a new wlan_AP_RSSI_MeasurementNotPossible_r13Type from encoded stream.
   */
  public static wlan_AP_RSSI_MeasurementNotPossible_r13Type fromPerAligned(byte[] encodedBytes) {
    wlan_AP_RSSI_MeasurementNotPossible_r13Type result = new wlan_AP_RSSI_MeasurementNotPossible_r13Type();
    result.decodePerAligned(new BitStreamReader(encodedBytes));
    return result;
  }

  @Override public Iterable<BitStream> encodePerUnaligned() {
    return super.encodePerUnaligned();
  }

  @Override public Iterable<BitStream> encodePerAligned() {
    return super.encodePerAligned();
  }

  @Override public void decodePerUnaligned(BitStreamReader reader) {
    super.decodePerUnaligned(reader);
  }

  @Override public void decodePerAligned(BitStreamReader reader) {
    super.decodePerAligned(reader);
  }

  @Override public String toString() {
    return toIndentedString("");
  }

  public String toIndentedString(String indent) {
    return "wlan_AP_RSSI_MeasurementNotPossible_r13Type (null value);\n";
  }
}

  
  // Copyright 2008 Google Inc. All Rights Reserved.
/*
 * This class is AUTOMATICALLY GENERATED. Do NOT EDIT.
 */


//

/**
 * 
 */
public static class wlan_AP_RTT_MeasurementNotPossible_r13Type extends Asn1Null {
  //

  private static final Asn1Tag TAG_wlan_AP_RTT_MeasurementNotPossible_r13Type
      = Asn1Tag.fromClassAndNumber(-1, -1);

  public wlan_AP_RTT_MeasurementNotPossible_r13Type() {
    super();
  }

  @Override
  @Nullable
  protected Asn1Tag getTag() {
    return TAG_wlan_AP_RTT_MeasurementNotPossible_r13Type;
  }

  @Override
  protected boolean isTagImplicit() {
    return true;
  }

  public static Collection<Asn1Tag> getPossibleFirstTags() {
    if (TAG_wlan_AP_RTT_MeasurementNotPossible_r13Type != null) {
      return ImmutableList.of(TAG_wlan_AP_RTT_MeasurementNotPossible_r13Type);
    } else {
      return Asn1Null.getPossibleFirstTags();
    }
  }

  /**
   * Creates a new wlan_AP_RTT_MeasurementNotPossible_r13Type from encoded stream.
   */
  public static wlan_AP_RTT_MeasurementNotPossible_r13Type fromPerUnaligned(byte[] encodedBytes) {
    wlan_AP_RTT_MeasurementNotPossible_r13Type result = new wlan_AP_RTT_MeasurementNotPossible_r13Type();
    result.decodePerUnaligned(new BitStreamReader(encodedBytes));
    return result;
  }

  /**
   * Creates a new wlan_AP_RTT_MeasurementNotPossible_r13Type from encoded stream.
   */
  public static wlan_AP_RTT_MeasurementNotPossible_r13Type fromPerAligned(byte[] encodedBytes) {
    wlan_AP_RTT_MeasurementNotPossible_r13Type result = new wlan_AP_RTT_MeasurementNotPossible_r13Type();
    result.decodePerAligned(new BitStreamReader(encodedBytes));
    return result;
  }

  @Override public Iterable<BitStream> encodePerUnaligned() {
    return super.encodePerUnaligned();
  }

  @Override public Iterable<BitStream> encodePerAligned() {
    return super.encodePerAligned();
  }

  @Override public void decodePerUnaligned(BitStreamReader reader) {
    super.decodePerUnaligned(reader);
  }

  @Override public void decodePerAligned(BitStreamReader reader) {
    super.decodePerAligned(reader);
  }

  @Override public String toString() {
    return toIndentedString("");
  }

  public String toIndentedString(String indent) {
    return "wlan_AP_RTT_MeasurementNotPossible_r13Type (null value);\n";
  }
}

  

    

  @Override public Iterable<BitStream> encodePerUnaligned() {
    return super.encodePerUnaligned();
  }

  @Override public Iterable<BitStream> encodePerAligned() {
    return super.encodePerAligned();
  }

  @Override public void decodePerUnaligned(BitStreamReader reader) {
    super.decodePerUnaligned(reader);
  }

  @Override public void decodePerAligned(BitStreamReader reader) {
    super.decodePerAligned(reader);
  }

  @Override public String toString() {
    return toIndentedString("");
  }

  public String toIndentedString(String indent) {
    StringBuilder builder = new StringBuilder();
    builder.append("WLAN_TargetDeviceErrorCauses_r13 = {\n");
    final String internalIndent = indent + "  ";
    for (SequenceComponent component : getComponents()) {
      if (component.isExplicitlySet()) {
        builder.append(internalIndent)
            .append(component.toIndentedString(internalIndent));
      }
    }
    if (isExtensible()) {
      builder.append(internalIndent).append("...\n");
      for (SequenceComponent component : getExtensionComponents()) {
        if (component.isExplicitlySet()) {
          builder.append(internalIndent)
              .append(component.toIndentedString(internalIndent));
        }
      }
    }
    builder.append(indent).append("};\n");
    return builder.toString();
  }
}
