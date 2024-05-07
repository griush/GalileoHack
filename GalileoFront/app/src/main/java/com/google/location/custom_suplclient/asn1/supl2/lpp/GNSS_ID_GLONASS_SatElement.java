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
import com.google.location.custom_suplclient.asn1.base.Asn1Integer;
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
public  class GNSS_ID_GLONASS_SatElement extends Asn1Sequence {
  //

  private static final Asn1Tag TAG_GNSS_ID_GLONASS_SatElement
      = Asn1Tag.fromClassAndNumber(-1, -1);

  public GNSS_ID_GLONASS_SatElement() {
    super();
  }

  @Override
  @Nullable
  protected Asn1Tag getTag() {
    return TAG_GNSS_ID_GLONASS_SatElement;
  }

  @Override
  protected boolean isTagImplicit() {
    return true;
  }

  public static Collection<Asn1Tag> getPossibleFirstTags() {
    if (TAG_GNSS_ID_GLONASS_SatElement != null) {
      return ImmutableList.of(TAG_GNSS_ID_GLONASS_SatElement);
    } else {
      return Asn1Sequence.getPossibleFirstTags();
    }
  }

  /**
   * Creates a new GNSS_ID_GLONASS_SatElement from encoded stream.
   */
  public static GNSS_ID_GLONASS_SatElement fromPerUnaligned(byte[] encodedBytes) {
    GNSS_ID_GLONASS_SatElement result = new GNSS_ID_GLONASS_SatElement();
    result.decodePerUnaligned(new BitStreamReader(encodedBytes));
    return result;
  }

  /**
   * Creates a new GNSS_ID_GLONASS_SatElement from encoded stream.
   */
  public static GNSS_ID_GLONASS_SatElement fromPerAligned(byte[] encodedBytes) {
    GNSS_ID_GLONASS_SatElement result = new GNSS_ID_GLONASS_SatElement();
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

  
  private SV_ID svID_;
  public SV_ID getSvID() {
    return svID_;
  }
  /**
   * @throws ClassCastException if value is not a SV_ID
   */
  public void setSvID(Asn1Object value) {
    this.svID_ = (SV_ID) value;
  }
  public SV_ID setSvIDToNewInstance() {
    svID_ = new SV_ID();
    return svID_;
  }
  
  private GNSS_SignalIDs signalsAvailable_;
  public GNSS_SignalIDs getSignalsAvailable() {
    return signalsAvailable_;
  }
  /**
   * @throws ClassCastException if value is not a GNSS_SignalIDs
   */
  public void setSignalsAvailable(Asn1Object value) {
    this.signalsAvailable_ = (GNSS_SignalIDs) value;
  }
  public GNSS_SignalIDs setSignalsAvailableToNewInstance() {
    signalsAvailable_ = new GNSS_SignalIDs();
    return signalsAvailable_;
  }
  
  private GNSS_ID_GLONASS_SatElement.channelNumberType channelNumber_;
  public GNSS_ID_GLONASS_SatElement.channelNumberType getChannelNumber() {
    return channelNumber_;
  }
  /**
   * @throws ClassCastException if value is not a GNSS_ID_GLONASS_SatElement.channelNumberType
   */
  public void setChannelNumber(Asn1Object value) {
    this.channelNumber_ = (GNSS_ID_GLONASS_SatElement.channelNumberType) value;
  }
  public GNSS_ID_GLONASS_SatElement.channelNumberType setChannelNumberToNewInstance() {
    channelNumber_ = new GNSS_ID_GLONASS_SatElement.channelNumberType();
    return channelNumber_;
  }
  

  

  

  @Override public Iterable<? extends SequenceComponent> getComponents() {
    ImmutableList.Builder<SequenceComponent> builder = ImmutableList.builder();
    
    builder.add(new SequenceComponent() {
          Asn1Tag tag = Asn1Tag.fromClassAndNumber(2, 0);

          @Override public boolean isExplicitlySet() {
            return getSvID() != null;
          }

          @Override public boolean hasDefaultValue() {
            return false;
          }

          @Override public boolean isOptional() {
            return false;
          }

          @Override public Asn1Object getComponentValue() {
            return getSvID();
          }

          @Override public void setToNewInstance() {
            setSvIDToNewInstance();
          }

          @Override public Collection<Asn1Tag> getPossibleFirstTags() {
            return tag == null ? SV_ID.getPossibleFirstTags() : ImmutableList.of(tag);
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
                return "svID : "
                    + getSvID().toIndentedString(indent);
              }
        });
    
    builder.add(new SequenceComponent() {
          Asn1Tag tag = Asn1Tag.fromClassAndNumber(2, 1);

          @Override public boolean isExplicitlySet() {
            return getSignalsAvailable() != null;
          }

          @Override public boolean hasDefaultValue() {
            return false;
          }

          @Override public boolean isOptional() {
            return false;
          }

          @Override public Asn1Object getComponentValue() {
            return getSignalsAvailable();
          }

          @Override public void setToNewInstance() {
            setSignalsAvailableToNewInstance();
          }

          @Override public Collection<Asn1Tag> getPossibleFirstTags() {
            return tag == null ? GNSS_SignalIDs.getPossibleFirstTags() : ImmutableList.of(tag);
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
                return "signalsAvailable : "
                    + getSignalsAvailable().toIndentedString(indent);
              }
        });
    
    builder.add(new SequenceComponent() {
          Asn1Tag tag = Asn1Tag.fromClassAndNumber(2, 2);

          @Override public boolean isExplicitlySet() {
            return getChannelNumber() != null;
          }

          @Override public boolean hasDefaultValue() {
            return false;
          }

          @Override public boolean isOptional() {
            return true;
          }

          @Override public Asn1Object getComponentValue() {
            return getChannelNumber();
          }

          @Override public void setToNewInstance() {
            setChannelNumberToNewInstance();
          }

          @Override public Collection<Asn1Tag> getPossibleFirstTags() {
            return tag == null ? GNSS_ID_GLONASS_SatElement.channelNumberType.getPossibleFirstTags() : ImmutableList.of(tag);
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
                return "channelNumber : "
                    + getChannelNumber().toIndentedString(indent);
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
 * This class is AUTOMATICALLY GENERATED. Do NOT EDIT.
 */


//

/**
 * 
 */
public static class channelNumberType extends Asn1Integer {
  //

  private static final Asn1Tag TAG_channelNumberType
      = Asn1Tag.fromClassAndNumber(-1, -1);

  public channelNumberType() {
    super();
    setValueRange(new java.math.BigInteger("-7"), new java.math.BigInteger("13"));

  }

  @Override
  @Nullable
  protected Asn1Tag getTag() {
    return TAG_channelNumberType;
  }

  @Override
  protected boolean isTagImplicit() {
    return true;
  }

  public static Collection<Asn1Tag> getPossibleFirstTags() {
    if (TAG_channelNumberType != null) {
      return ImmutableList.of(TAG_channelNumberType);
    } else {
      return Asn1Integer.getPossibleFirstTags();
    }
  }

  /**
   * Creates a new channelNumberType from encoded stream.
   */
  public static channelNumberType fromPerUnaligned(byte[] encodedBytes) {
    channelNumberType result = new channelNumberType();
    result.decodePerUnaligned(new BitStreamReader(encodedBytes));
    return result;
  }

  /**
   * Creates a new channelNumberType from encoded stream.
   */
  public static channelNumberType fromPerAligned(byte[] encodedBytes) {
    channelNumberType result = new channelNumberType();
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
    return "channelNumberType = " + getInteger() + ";\n";
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
    builder.append("GNSS_ID_GLONASS_SatElement = {\n");
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
